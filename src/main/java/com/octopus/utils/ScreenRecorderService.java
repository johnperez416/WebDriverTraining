package com.octopus.utils;

import java.io.File;

public interface ScreenRecorderService {
    File start(final File file);
    void stop();
}
