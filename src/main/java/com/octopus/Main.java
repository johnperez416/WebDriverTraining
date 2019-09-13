package com.octopus;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            cucumber.api.cli.Main.run(
                    new String[]{
                            "--monochrome",
                            "--glue", "com.octopus.decoratorbase",
                            args[0]},
                    Thread.currentThread().getContextClassLoader());
        } catch (IOException ex) {
            System.exit(1);
        }
    }
}
