package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.utils.EnvironmentAliasesProcessor;

import java.util.Map;
import java.util.stream.Collectors;

public class EnvironmentAliasesProcessorImpl implements EnvironmentAliasesProcessor {
    @Override
    public void addEnvirtonmentVarsAsAliases() {
        /*
            Take any environment variable with the prefix "CucumberAlias-" and set it as an
            alias value in the AutomatedBrowserBase class.
         */
        AutomatedBrowserBase.setExternalAliases(
                System.getenv().entrySet().stream()
                        .filter(s -> s.getKey().startsWith(Constants.ALIAS_HEADER_PREFIX))
                        .collect(Collectors.toMap(x ->
                                        x.getKey().replaceAll(Constants.ALIAS_HEADER_PREFIX, ""),
                                x -> x.getValue())));

    }

    @Override
    public void addHeaderVarsAsAliases(Map<String, String> headers) {
        /*
            Take any header with the prefix "CucumberAlias-" and set it as an
            alias value in the AutomatedBrowserBase class.
        */
        if (headers != null) {
            AutomatedBrowserBase.setExternalAliases(
                    headers.entrySet().stream()
                            .filter(s -> s.getKey().startsWith(Constants.ALIAS_HEADER_PREFIX))
                            .collect(Collectors.toMap(x ->
                                            x.getKey().replaceAll(Constants.ALIAS_HEADER_PREFIX, ""),
                                    x -> x.getValue())));
        }
    }
}
