package com.octopus.utils;

public interface OSUtils {
    /**
     * Sanitize a filename so it can be saved to disk.
     *
     * @param file The filename to fix.
     * @return A sanitized filename.
     */
    String fixFileName(String file);
}
