package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.utils.GithubActionsServiceMessageGenerator;
import com.octopus.utils.SystemPropertyUtils;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class GithubActionsServiceMessageGeneratorImpl implements GithubActionsServiceMessageGenerator {
    private static final Logger LOGGER = Logger.getLogger(GithubActionsServiceMessageGeneratorImpl.class.toString());
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    @Override
    public void setEnvironmentVariable(final String name, final String value) {
        checkNotNull(value);
        checkNotNull(name);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::set-env name=" + name + "::" + value);
    }

    @Override
    public void setOutputParameter(final String name, final String value) {
        checkNotNull(value);
        checkNotNull(name);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::set-output name=" + name + "::" + value);
    }

    @Override
    public void addSystemPath(final String path) {
        checkNotNull(path);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::add-path::" + path);
    }

    @Override
    public void setDebugMessage(final String message) {
        checkNotNull(message);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::debug::" + message);
    }

    @Override
    public void setWarningMessage(final String message) {
        checkNotNull(message);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::warning::" + message);
    }

    @Override
    public void setErrorMessage(final String message) {
        checkNotNull(message);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::error::" + message);
    }

    @Override
    public void maskValue(final String value) {
        checkNotNull(value);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::add-mask::" + value);
    }

    @Override
    public void stopLogging(final String token) {
        checkNotNull(token);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::stop-commands::" + token);
    }

    @Override
    public void startLogging(final String token) {
        checkNotNull(token);

        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SERVICEMESSAGES, false)) {
            return;
        }

        LOGGER.info("::" + token + "::");
    }
}
