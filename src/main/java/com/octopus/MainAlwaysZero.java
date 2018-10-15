package com.octopus;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

/**
 * When running K8S jobs, a non-zero return code will result in the the job being restarted.
 * A failed test does not necessarily mean the test failed to execute properly, so this
 * class provides an entry point that always returns 0 even if the test failed.
 */
public class MainAlwaysZero {
    public static void main(final String[] args) throws IOException {
        cucumber.api.cli.Main.run(
                ArrayUtils.addAll(args, new String[]{"--glue", "com.octopus.decoratorbase"}),
                Thread.currentThread().getContextClassLoader());
    }
}
