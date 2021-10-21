package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.exceptions.ScriptException;
import com.octopus.utils.JavaLauncherUtils;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class JavaLauncherUtilsImpl implements JavaLauncherUtils {
    @Override
    public int launchAppExternally(final String args) {
        try {
            // java binary
            final String java = System.getProperty("java.home") + "/bin/java";
            // vm arguments
            final List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            final StringBuffer vmArgsOneLine = new StringBuffer();
            for (final String arg : vmArguments) {
                // if it's the agent argument : we ignore it otherwise the
                // address of the old application and the new one will be in conflict
                if (!arg.contains("-agentlib")) {
                    vmArgsOneLine.append(arg);
                    vmArgsOneLine.append(" ");
                }
            }
            // init the command to execute, add the vm args
            final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
            // program main and program arguments (be careful a sun property. might not be supported by all JVM)
            final String[] mainCommand = System.getProperty("sun.java.command").split(" ");
            // program main is a jar
            if (mainCommand[0].endsWith(".jar")) {
                // if it's a jar, add -jar mainJar
                cmd.append("-jar " + new File(mainCommand[0]).getPath());
            } else {
                // else it's a .class, add the classpath and mainClass
                cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
            }

            // First see if the supplied file is an absolute path, otherwise assume it is in the same directory as the current feature file
            if (StringUtils.isBlank(args)) {
                cmd.append(" ");
                cmd.append(args);
            }

            return Runtime.getRuntime().exec(cmd.toString()).waitFor();
        } catch (final IOException | InterruptedException ex) {
            throw new ScriptException("Failed to run nested feature file.", ex);
        }
    }

    @Override
    public int launchAppInternally(final String[] args) {
        final ArrayList<String> options = new ArrayList<>(Constants.DEFAULT_CUCUMBER_OPTIONS);
        Collections.addAll(options, args);
        return io.cucumber.core.cli.Main.run(
                options.toArray(new String[0]),
                Thread.currentThread().getContextClassLoader());
    }
}
