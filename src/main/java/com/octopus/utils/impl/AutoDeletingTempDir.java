package com.octopus.utils.impl;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class AutoDeletingTempDir implements AutoCloseable {

    private final File file;

    public AutoDeletingTempDir() {
        file = Files.createTempDir();
    }

    public AutoDeletingTempDir(final File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public void close() {
        FileUtils.deleteQuietly(file);
    }
}