package com.octopus.utils.impl;

import com.google.common.io.Files;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class AutoDeletingTempDir implements AutoCloseable {

    /**
     * The directory that is deleted on close.
     */
    private final File file;


    /**
     * Default constructor. Creates a random directory.
     */
    public AutoDeletingTempDir() {
        file = Files.createTempDir();
    }


    /**
     * Custoemr constructor to track an existing file.
     *
     * @param file The existing file to delete on close.
     */
    public AutoDeletingTempDir(final File file) {
        this.file = file;
    }

    /**
     * @return the directory to delete on close.
     */
    public File getFile() {
        return file;
    }

    @Override
    public void close() {
        FileUtils.deleteQuietly(file);
    }
}