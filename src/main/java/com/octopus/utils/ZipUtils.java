package com.octopus.utils;

import java.io.IOException;

public interface ZipUtils {
    void unzipFile(final String fileZip, final String outputDirectory) throws IOException;
    void zipDirectory(final String fileZip, final String inputDirectory) throws IOException;
}