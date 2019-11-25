package com.octopus.utils;

import java.io.File;

public interface ServiceMessageGenerator {
    void newArtifact(String path, String name);
    void newArtifact(File path, String name);
}
