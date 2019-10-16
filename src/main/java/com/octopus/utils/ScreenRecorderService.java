package com.octopus.utils;

import java.io.File;

public interface ScreenRecorderService {
    void start(final File file);
    void stop();
}
