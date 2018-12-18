package com.octopus;

import com.octopus.utils.EnvironmentAliasesProcessor;
import com.octopus.utils.impl.EnvironmentAliasesProcessorImpl;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

public class Main {
    private static final EnvironmentAliasesProcessor ENVIRONMENT_ALIASES_PROCESSOR =
            new EnvironmentAliasesProcessorImpl();

    public static void main(final String[] args) throws IOException {
        ENVIRONMENT_ALIASES_PROCESSOR.addEnvirtonmentVarsAsAliases();
        System.exit(cucumber.api.cli.Main.run(
                ArrayUtils.addAll(args, new String[]{"--glue", "com.octopus.decoratorbase"}),
                Thread.currentThread().getContextClassLoader())
        );
    }
}
