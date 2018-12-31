package com.octopus.utils.impl;

import com.octopus.utils.ZipUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtilsImpl implements ZipUtils {
    public void unzipFile(final String fileZip, final String outputDirectory) throws IOException {

        final byte[] buffer = new byte[1024];

        try (final ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                final String fileName = zipEntry.getName();
                final File newFile = new File(outputDirectory + "/" + fileName);
                try (final FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    public void zipDirectory(final String fileZip, final String inputDirectory) throws IOException {
        final File inputDirectoryFile = new File(inputDirectory);
        final byte[] buffer = new byte[1024];

        try (FileOutputStream fos = new FileOutputStream(fileZip)) {
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (final File file : FileUtils.listFiles(inputDirectoryFile, null, true)) {
                    final ZipEntry ze = new ZipEntry(file.toPath().relativize(inputDirectoryFile.toPath()).toString());
                    zos.putNextEntry(ze);
                    try (FileInputStream in = new FileInputStream(file.getPath())) {
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                }
                zos.closeEntry();
            }
        }
    }
}
