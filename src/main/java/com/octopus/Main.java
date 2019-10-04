package com.octopus;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.decorators.WebDriverDecorator;
import com.octopus.utils.EnvironmentAliasesProcessor;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.EnvironmentAliasesProcessorImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.vavr.control.Try;

import java.util.ArrayList;
import java.util.Collections;

import static com.octopus.Constants.BROWSER_CLEANUP;

public class Main {
    private static final EnvironmentAliasesProcessor ENVIRONMENT_ALIASES_PROCESSOR =
            new EnvironmentAliasesProcessorImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    public static void main(String[] args) {

        try {
            int retValue = 0;
            for (int x = 0; x < SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.RETRY_COUNT, 1); ++x) {
                final ArrayList<String> options = new ArrayList<>() {{
                    add("--glue");
                    add("com.octopus.decoratorbase");
                    add("--strict");
                }};

                Collections.addAll(options, args);

                ENVIRONMENT_ALIASES_PROCESSOR.addSystemPropVarsAsAliases();

                retValue = io.cucumber.core.cli.Main.run(
                        options.toArray(new String[0]),
                        Thread.currentThread().getContextClassLoader());

                if (retValue == 0) {
                    break;
                }

                Try.run(() -> Thread.sleep(Constants.RETRY_DELAY));
            }

            System.exit(retValue);
        } finally {
            WebDriverDecorator.staticStopScreenRecording();
            if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(BROWSER_CLEANUP, true)) {
                if (AutomatedBrowserBase.getInstance() != null) {
                    AutomatedBrowserBase.getInstance().closeBrowser();
                }
            }
        }
    }
}
