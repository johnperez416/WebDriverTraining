package com.octopus.logging;

import java.util.logging.*;

/**
 * A console logger that uses system.out
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
