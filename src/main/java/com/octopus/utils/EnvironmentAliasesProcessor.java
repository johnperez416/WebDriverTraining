package com.octopus.utils;

import java.util.Map;

public interface EnvironmentAliasesProcessor {
    void addEnvironmentVarsAsAliases();
    void addHeaderVarsAsAliases(Map<String, String> headers);
    void addSystemPropVarsAsAliases();
}
