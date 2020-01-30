package com.octopus;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.decorators.WebDriverDecorator;
import com.octopus.utils.EnvironmentAliasesProcessor;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.EnvironmentAliasesProcessorImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.vavr.control.Try;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.octopus.Constants.BROWSER_CLEANUP;
import static com.octopus.Constants.DUMP_OPTIONS;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.toString());
    private static final EnvironmentAliasesProcessor ENVIRONMENT_ALIASES_PROCESSOR = new EnvironmentAliasesProcessorImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    /**
     * We track the args so we can relaunch Cucumber with the same options
     */
    public static String[] args;

    public static void main(final String[] args) {
        Main.args = args;
        catchShutdown();
        configureLogging();
        dumpOptions();
        final int retValue = runCucumber(args);
        System.exit(retValue);
    }

    private static void catchShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    private static int runCucumber(final String[] args) {
        try {
            int retValue = 0;
            final ArrayList<String> options = new ArrayList<>(Constants.DEFAULT_CUCUMBER_OPTIONS);
            Collections.addAll(options, args);

            ENVIRONMENT_ALIASES_PROCESSOR.addSystemPropVarsAsAliases();

            for (int x = 0; x < SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.RETRY_COUNT, 1); ++x) {
                retValue = io.cucumber.core.cli.Main.run(
                        options.toArray(new String[0]),
                        Thread.currentThread().getContextClassLoader());

                if (retValue == 0) {
                    break;
                }

                Try.run(() -> Thread.sleep(Constants.RETRY_DELAY));
            }
            return retValue;
        } finally {
            shutdown();
        }
    }

    private static void shutdown() {
        Try.run(() -> {
            WebDriverDecorator.staticStopScreenRecording();
            if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(BROWSER_CLEANUP, true)) {
                if (AutomatedBrowserBase.getInstance() != null) {
                    AutomatedBrowserBase.getInstance().closeBrowser();
                }
            }
        });
    }

    private static void configureLogging() {
        Try.run(() -> LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties")));
        // Disable some logs from BrowserMob
        System.setProperty("io.netty.leakDetection.level", "DISABLED");
    }

    private static void dumpOptions() {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(DUMP_OPTIONS, false)) {
            LOGGER.info("Video recording " + (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_VIDEO_RECORDING, false) ? "disabled" : "enabled"));
            LOGGER.info("Screenshots " + (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SCREENSHOTS, false) ? "disabled" : "enabled"));
            LOGGER.info("Highlights " + (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_HIGHLIGHTS, false) ? "disabled" : "enabled"));
            LOGGER.info("Headless Environment " + GraphicsEnvironment.isHeadless());
        }
    }
}
