package com.octopus;

import com.octopus.decorators.WebDriverDecorator;
import com.octopus.utils.EnvironmentAliasesProcessor;
import com.octopus.utils.impl.EnvironmentAliasesProcessorImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    private static final EnvironmentAliasesProcessor ENVIRONMENT_ALIASES_PROCESSOR =
            new EnvironmentAliasesProcessorImpl();

    public static void main(String[] args) {
        try {
            final ArrayList<String> options = new ArrayList<>() {{
                add("--glue");
                add("com.octopus.decoratorbase");
                add("--strict");
            }};

            Collections.addAll(options, args);

            ENVIRONMENT_ALIASES_PROCESSOR.addSystemPropVarsAsAliases();

            final int retValue = io.cucumber.core.cli.Main.run(
                    options.toArray(new String[0]),
                    Thread.currentThread().getContextClassLoader());

            System.exit(retValue);
        } finally {
            WebDriverDecorator.staticStopScreenRecording();
        }
    }
}
