package com.octopus.utils.impl;

import com.octopus.utils.OSUtils;
import org.apache.commons.lang3.SystemUtils;

public class OSUtilsImpl implements OSUtils {
    @Override
    public String fixFileName(String file) {
        return SystemUtils.IS_OS_WINDOWS ?
                file.replaceAll("/", "\\\\") :
                file;
    }
}
