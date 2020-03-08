package io.cucumber.core.plugin;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cucumber.core.exception.CucumberException;
import io.cucumber.core.internal.gherkin.ast.Step;
import io.cucumber.core.internal.gherkin.ast.*;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Locale.ROOT;

/**
 * A copy of HTMLFormatter (https://github.com/cucumber/cucumber-jvm/blob/master/core/src/main/java/io/cucumber/core/plugin/HTMLFormatter.java)
 * that embeds all the resources to create a self contained HTML file.
 */
public final class InlineHTMLFormatter implements EventListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String JS_FORMATTER_VAR = "formatter";
    private static final String HTML_TEMPLATE = "io/cucumber/core/plugin/inlinehtml/index.html";
    private static final String JS_REFERENCE = "<script src=\"report.js\"></script>";
    private static final Map<String, String> MIME_TYPES_EXTENSIONS = new HashMap<String, String>() {
        {
            put("image/bmp", "bmp");
            put("image/gif", "gif");
            put("image/jpeg", "jpg");
            put("image/png", "png");
            put("image/svg+xml", "svg");
            put("video/ogg", "ogg");
        }
    };

    private final TestSourcesModel testSources = new TestSourcesModel();
    private URL htmlReportDir;
    private final ByteArrayOutputStream jsContent = new ByteArrayOutputStream();
    private final NiceAppendable jsOut = new NiceAppendable(new OutputStreamWriter(jsContent));


    private boolean firstFeature = true;
    private URI currentFeatureFile;
    private Map<String, Object> currentTestCaseMap;
    private ScenarioOutline currentScenarioOutline;
    private Examples currentExamples;

    @SuppressWarnings("WeakerAccess") // Used by PluginFactory
    public InlineHTMLFormatter(final URL htmlReportDir) {
        this.htmlReportDir = htmlReportDir;
    }

    private static String printStackTrace(final Throwable error) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        error.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private static void writeStreamToURL(final InputStream in, final URL url) {
        final OutputStream out = createReportFileOutputStream(url);

        final byte[] buffer = new byte[16 * 1024];
        try {
            int len = in.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = in.read(buffer);
            }
        } catch (IOException e) {
            throw new CucumberException("Unable to write to report file item: ", e);
        } finally {
            closeQuietly(out);
        }
    }

    private static void writeBytesToURL(final byte[] buf, final URL url) throws CucumberException {
        final OutputStream out = createReportFileOutputStream(url);
        try {
            out.write(buf);
        } catch (IOException e) {
            throw new CucumberException("Unable to write to report file item: ", e);
        } finally {
            closeQuietly(out);
        }
    }

    private static OutputStream createReportFileOutputStream(final URL url) {
        try {
            return new URLOutputStream(url);
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private static void closeQuietly(final Closeable out) {
        try {
            out.close();
        } catch (IOException ignored) {
            // go gentle into that good night
        }
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(EmbedEvent.class, this::handleEmbed);
        publisher.registerHandlerFor(WriteEvent.class, this::handleWrite);
        publisher.registerHandlerFor(TestRunFinished.class, event -> finishReport());
    }

    private void handleTestSourceRead(final TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.getUri(), event);
    }

    private void handleTestCaseStarted(final TestCaseStarted event) {
        if (firstFeature) {
            jsOut.append("$(document).ready(function() {").append("var ")
                    .append(JS_FORMATTER_VAR).append(" = new CucumberHTML.DOMFormatter($('.cucumber-report'));");
            firstFeature = false;
        }
        handleStartOfFeature(event.getTestCase());
        handleScenarioOutline(event.getTestCase());
        currentTestCaseMap = createTestCase(event.getTestCase());
        if (testSources.hasBackground(currentFeatureFile, event.getTestCase().getLine())) {
            jsFunctionCall("background", createBackground(event.getTestCase()));
        } else {
            jsFunctionCall("scenario", currentTestCaseMap);
            currentTestCaseMap = null;
        }
    }

    private void finishReport() {
        if (!firstFeature) {
            jsOut.append("});");
            copyReportFiles();
        }
        jsOut.close();
    }

    private void handleTestStepStarted(final TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            final PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            if (isFirstStepAfterBackground(testStep)) {
                jsFunctionCall("scenario", currentTestCaseMap);
                currentTestCaseMap = null;
            }
            jsFunctionCall("step", createTestStep(testStep));
            jsFunctionCall("match", createMatchMap((PickleStepTestStep) event.getTestStep()));
        }
    }

    private void handleTestStepFinished(final TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            jsFunctionCall("result", createResultMap(event.getResult()));
        } else if (event.getTestStep() instanceof HookTestStep) {
            final HookTestStep hookTestStep = (HookTestStep) event.getTestStep();
            jsFunctionCall(getFunctionName(hookTestStep), createResultMap(event.getResult()));
        } else {
            throw new IllegalStateException();
        }
    }

    private String getFunctionName(final HookTestStep hookTestStep) {
        final HookType hookType = hookTestStep.getHookType();
        switch (hookType) {
            case BEFORE:
                return "before";
            case AFTER:
                return "after";
            case BEFORE_STEP:
                return "beforestep";
            case AFTER_STEP:
                return "afterstep";
            default:
                throw new IllegalArgumentException(hookType.name());
        }
    }

    private void handleEmbed(final EmbedEvent event) {
        final String mediaType = event.getMediaType();
        // just pass straight to the plugin to output in the html
        jsFunctionCall("embedding", mediaType, new String(event.getData()), event.getName());

    }

    private void handleWrite(final WriteEvent event) {
        jsFunctionCall("write", event.getText());
    }

    private void handleStartOfFeature(final TestCase testCase) {
        if (currentFeatureFile == null || !currentFeatureFile.equals(testCase.getUri())) {
            currentFeatureFile = testCase.getUri();
            jsFunctionCall("uri", TestSourcesModel.relativize(currentFeatureFile));
            jsFunctionCall("feature", createFeature(testCase));
        }
    }

    private Map<String, Object> createFeature(final TestCase testCase) {
        final Map<String, Object> featureMap = new HashMap<>();
        final Feature feature = testSources.getFeature(testCase.getUri());
        if (feature != null) {
            featureMap.put("keyword", feature.getKeyword());
            featureMap.put("name", feature.getName());
            featureMap.put("description", feature.getDescription() != null ? feature.getDescription() : "");
            if (!feature.getTags().isEmpty()) {
                featureMap.put("tags", createTagList(feature.getTags()));
            }
        }
        return featureMap;
    }

    private List<Map<String, Object>> createTagList(final List<Tag> tags) {
        final List<Map<String, Object>> tagList = new ArrayList<>();
        for (Tag tag : tags) {
            final Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("name", tag.getName());
            tagList.add(tagMap);
        }
        return tagList;
    }

    private void handleScenarioOutline(final TestCase testCase) {
        final TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (TestSourcesModel.isScenarioOutlineScenario(astNode)) {
            final ScenarioOutline scenarioOutline = (ScenarioOutline) TestSourcesModel.getScenarioDefinition(astNode);
            if (currentScenarioOutline == null || !currentScenarioOutline.equals(scenarioOutline)) {
                currentScenarioOutline = scenarioOutline;
                jsFunctionCall("scenarioOutline", createScenarioOutline(currentScenarioOutline));
                addOutlineStepsToReport(scenarioOutline);
            }
            final Examples examples = (Examples) astNode.parent.node;
            if (currentExamples == null || !currentExamples.equals(examples)) {
                currentExamples = examples;
                jsFunctionCall("examples", createExamples(currentExamples));
            }
        } else {
            currentScenarioOutline = null;
            currentExamples = null;
        }
    }

    private Map<String, Object> createScenarioOutline(final ScenarioOutline scenarioOutline) {
        final Map<String, Object> scenarioOutlineMap = new HashMap<>();
        scenarioOutlineMap.put("name", scenarioOutline.getName());
        scenarioOutlineMap.put("keyword", scenarioOutline.getKeyword());
        scenarioOutlineMap.put("description", scenarioOutline.getDescription() != null ? scenarioOutline.getDescription() : "");
        if (!scenarioOutline.getTags().isEmpty()) {
            scenarioOutlineMap.put("tags", createTagList(scenarioOutline.getTags()));
        }
        return scenarioOutlineMap;
    }

    private void addOutlineStepsToReport(final ScenarioOutline scenarioOutline) {
        for (Step step : scenarioOutline.getSteps()) {
            final Map<String, Object> stepMap = new HashMap<>();
            stepMap.put("name", step.getText());
            stepMap.put("keyword", step.getKeyword());
            if (step.getArgument() != null) {
                final Node argument = step.getArgument();
                if (argument instanceof DocString) {
                    stepMap.put("doc_string", createDocStringMap((DocString) argument));
                } else if (argument instanceof DataTable) {
                    stepMap.put("rows", createDataTableList((DataTable) argument));
                }
            }
            jsFunctionCall("step", stepMap);
        }
    }

    private Map<String, Object> createDocStringMap(final DocString docString) {
        final Map<String, Object> docStringMap = new HashMap<>();
        docStringMap.put("value", docString.getContent());
        return docStringMap;
    }

    private List<Map<String, Object>> createDataTableList(final DataTable dataTable) {
        final List<Map<String, Object>> rowList = new ArrayList<>();
        for (TableRow row : dataTable.getRows()) {
            rowList.add(createRowMap(row));
        }
        return rowList;
    }

    private Map<String, Object> createRowMap(final TableRow row) {
        final Map<String, Object> rowMap = new HashMap<>();
        rowMap.put("cells", createCellList(row));
        return rowMap;
    }

    private List<String> createCellList(final TableRow row) {
        final List<String> cells = new ArrayList<>();
        for (TableCell cell : row.getCells()) {
            cells.add(cell.getValue());
        }
        return cells;
    }

    private Map<String, Object> createExamples(final Examples examples) {
        final Map<String, Object> examplesMap = new HashMap<>();
        examplesMap.put("name", examples.getName());
        examplesMap.put("keyword", examples.getKeyword());
        examplesMap.put("description", examples.getDescription() != null ? examples.getDescription() : "");
        final List<Map<String, Object>> rowList = new ArrayList<>();
        rowList.add(createRowMap(examples.getTableHeader()));
        for (TableRow row : examples.getTableBody()) {
            rowList.add(createRowMap(row));
        }
        examplesMap.put("rows", rowList);
        if (!examples.getTags().isEmpty()) {
            examplesMap.put("tags", createTagList(examples.getTags()));
        }
        return examplesMap;
    }

    private Map<String, Object> createTestCase(final TestCase testCase) {
        final Map<String, Object> testCaseMap = new HashMap<>();
        testCaseMap.put("name", testCase.getName());
        final TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (astNode != null) {
            final ScenarioDefinition scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);
            testCaseMap.put("keyword", scenarioDefinition.getKeyword());
            testCaseMap.put("description", scenarioDefinition.getDescription() != null ? scenarioDefinition.getDescription() : "");
        }
        if (!testCase.getTags().isEmpty()) {
            final List<Map<String, Object>> tagList = new ArrayList<>();
            for (String tag : testCase.getTags()) {
                final Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("name", tag);
                tagList.add(tagMap);
            }
            testCaseMap.put("tags", tagList);
        }
        return testCaseMap;
    }

    private Map<String, Object> createBackground(final TestCase testCase) {
        final TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (astNode != null) {
            final Background background = TestSourcesModel.getBackgroundForTestCase(astNode);
            final Map<String, Object> testCaseMap = new HashMap<>();
            testCaseMap.put("name", background.getName());
            testCaseMap.put("keyword", background.getKeyword());
            testCaseMap.put("description", background.getDescription() != null ? background.getDescription() : "");
            return testCaseMap;
        }
        return null;
    }

    private boolean isFirstStepAfterBackground(final PickleStepTestStep testStep) {
        final TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
        if (astNode != null) {
            return currentTestCaseMap != null && !TestSourcesModel.isBackgroundStep(astNode);
        }
        return false;
    }

    private Map<String, Object> createTestStep(final PickleStepTestStep testStep) {
        final Map<String, Object> stepMap = new HashMap<>();
        stepMap.put("name", testStep.getStepText());
        final StepArgument argument = testStep.getStepArgument();
        if (argument != null) {
            if (argument instanceof DocStringArgument) {
                final DocStringArgument docStringArgument = (DocStringArgument) argument;
                stepMap.put("doc_string", createDocStringMap(docStringArgument));
            } else if (argument instanceof DataTableArgument) {
                final DataTableArgument dataTableArgument = (DataTableArgument) argument;
                stepMap.put("rows", createDataTableList(dataTableArgument));
            }
        }
        final TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
        if (astNode != null) {
            final Step step = (Step) astNode.node;
            stepMap.put("keyword", step.getKeyword());
        }

        return stepMap;
    }

    private Map<String, Object> createDocStringMap(final DocStringArgument docString) {
        final Map<String, Object> docStringMap = new HashMap<>();
        docStringMap.put("value", docString.getContent());
        return docStringMap;
    }

    private List<Map<String, Object>> createDataTableList(final DataTableArgument dataTable) {
        final List<Map<String, Object>> rowList = new ArrayList<>();
        for (List<String> row : dataTable.cells()) {
            rowList.add(createRowMap(row));
        }
        return rowList;
    }

    private Map<String, Object> createRowMap(final List<String> row) {
        final Map<String, Object> rowMap = new HashMap<>();
        rowMap.put("cells", row);
        return rowMap;
    }

    private Map<String, Object> createMatchMap(final PickleStepTestStep testStep) {
        final Map<String, Object> matchMap = new HashMap<>();
        final String location = testStep.getCodeLocation();
        if (location != null) {
            matchMap.put("location", location);
        }
        return matchMap;
    }

    private Map<String, Object> createResultMap(final Result result) {
        final Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", result.getStatus().name().toLowerCase(ROOT));
        if (result.getError() != null) {
            resultMap.put("error_message", printStackTrace(result.getError()));
        }
        return resultMap;
    }

    private void jsFunctionCall(final String functionName, final Object... args) {
        final NiceAppendable out = jsOut.append(JS_FORMATTER_VAR + ".").append(functionName).append("(");
        boolean comma = false;
        for (Object arg : args) {
            if (comma) {
                out.append(", ");
            }
            GSON.toJson(arg, out);
            comma = true;
        }
        out.append(");").println();
    }

    private void copyReportFiles() {
        if (htmlReportDir == null) {
            return;
        }

        try {
            final String html = Resources.toString(Resources.getResource(HTML_TEMPLATE), Charsets.UTF_8);
            final String processedHtml = html.replace(JS_REFERENCE, "<script>" + new String(jsContent.toByteArray()) + "</script>");
            writeBytesToURL(processedHtml.getBytes(), toUrl("index.html"));
        } catch (final IOException e) {
            throw new CucumberException(e);
        }
    }

    private URL toUrl(final String fileName) {
        try {
            return new URL(htmlReportDir, fileName);
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

}