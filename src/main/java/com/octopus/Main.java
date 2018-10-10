package com.octopus;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

public class Main {
    public static void main(final String[] args) throws IOException {
        System.exit(cucumber.api.cli.Main.run(
                ArrayUtils.addAll(args, new String[]{"--glue", "com.octopus.decoratorbase"}),
                Thread.currentThread().getContextClassLoader())
        );
    }
}
