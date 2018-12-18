package com.octopus.utils;

import java.util.Map;

public interface EnvironmentAliasesProcessor {
    void addEnvirtonmentVarsAsAliases();
    void addHeaderVarsAsAliases(Map<String, String> headers);
}
