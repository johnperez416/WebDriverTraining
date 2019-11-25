package com.octopus.utils.impl;

import com.octopus.utils.ServiceMessageGenerator;
import org.apache.commons.text.WordUtils;

import java.io.File;
import java.util.Base64;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServiceMessageGeneratorImpl implements ServiceMessageGenerator {
    private static final Logger LOGGER = Logger.getLogger(ServiceMessageGeneratorImpl.class.toString());

    @Override
    public void newArtifact(final String path, final String name) {
        newArtifact(new File(path), name);
    }

    @Override
    public void newArtifact(final File pathFile, final String name) {
        checkNotNull(pathFile);
        checkNotNull(name);

        try {
            final String fullPath = pathFile.getCanonicalPath();

            LOGGER.info("\n##octopus[createArtifact path='" +
                    Base64.getEncoder().encodeToString(fullPath.getBytes()) +
                    "' name='" +
                    Base64.getEncoder().encodeToString(name.getBytes()) +
                    "' length='" +
                    Base64.getEncoder().encodeToString((pathFile.length() + "").getBytes()) +
                    "']");
        } catch (final Exception ex) {
            LOGGER.severe("Failed to process the artifact at " + pathFile.getPath() + ". " + ex);
        }
    }

    @Override
    public void setProgress(final Integer percent, final String message) {
        LOGGER.info("\n##octopus[progress percentage='" +
                Base64.getEncoder().encodeToString(percent.toString().getBytes()) +
                "' message='" +
                Base64.getEncoder().encodeToString(message.getBytes()) +
                "']");
    }

    @Override
    public void newVariable(final String variable, final String value, final Boolean sensitive) {
        LOGGER.info("\n##octopus[setVariable name='"+
                Base64.getEncoder().encodeToString(variable.getBytes()) +
                "' value='"+
                Base64.getEncoder().encodeToString(value.getBytes()) +
                "' sensitive='" +
                Base64.getEncoder().encodeToString(WordUtils.capitalize(sensitive.toString()).getBytes()) +
                "']");
    }

    @Override
    public void newVariable(final String variable, final String value) {
        newVariable(variable, value, false);
    }
}
