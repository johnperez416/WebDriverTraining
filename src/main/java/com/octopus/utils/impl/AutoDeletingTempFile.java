package com.octopus.utils.impl;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class AutoDeletingTempFile implements AutoCloseable {

    private final File file;

    public AutoDeletingTempFile(final String prefix, final String suffix) throws IOException {
        file = File.createTempFile(prefix, suffix);
    }

    public AutoDeletingTempFile(final File file) {
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