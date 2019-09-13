package com.octopus;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            final ArrayList<String> options = new ArrayList<String>() {{
                add("--monochrome");
                add("--glue");
                add("com.octopus.decoratorbase");
            }};

            if (args.length >= 2) {
                options.add("--tags");
                options.add(args[1]);
            }

            options.add(args[0]);

            cucumber.api.cli.Main.run(
                    options.toArray(new String[0]),
                    Thread.currentThread().getContextClassLoader());
        } catch (IOException ex) {
            System.exit(1);
        }
    }
}
