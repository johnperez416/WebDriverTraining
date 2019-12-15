package com.octopus.utils;

public interface GithubActionsServiceMessageGenerator {
    void setEnvironmentVariable(String name, String value);
    void setOutputParameter(String name, String value);
    void addSystemPath(String path);
    void setDebugMessage(String message);
    void setWarningMessage(String message);
    void setErrorMessage(String message);
    void maskValue(String value);
    void stopLogging(String token);
    void startLogging(String token);

}
