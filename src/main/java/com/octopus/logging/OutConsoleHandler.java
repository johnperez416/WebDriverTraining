package com.octopus.logging;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * A console logger that uses system.out instead of system.err.
 */
public class OutConsoleHandler extends StreamHandler {

    public OutConsoleHandler() {
        super(System.out, new SimpleFormatter());
    }


    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }


    @Override
    public void close() {
        flush();
    }
}
