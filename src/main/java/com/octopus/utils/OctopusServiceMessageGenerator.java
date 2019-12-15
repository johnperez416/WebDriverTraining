package com.octopus.utils;

import java.io.File;

public interface OctopusServiceMessageGenerator {
    void newArtifact(String path, String name);
    void newArtifact(File path, String name);
    void setProgress(Integer percent, String message);
    void newVariable(String variable, String value, Boolean sensitive);
    void newVariable(String variable, String value);
}
