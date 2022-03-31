package com.octopus.utils.impl;

import static com.google.common.base.Preconditions.checkArgument;

import com.octopus.utils.SystemPropertyUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Implementation that deals with the restrictions imposed by web start
 */
public class SystemPropertyUtilsImpl implements SystemPropertyUtils {

    /**
     * These are the prefixes that can be applied to any system property. This is mostly to
     * facilitate web start, which has restrictions on system properties. See
     * http://stackoverflow.com/questions/19400725/with-java-7-update-45-the-system-properties-no-longer-set-from-jnlp-tag-proper
     * for details.
     */
    private static final List<String> SYSTEM_PROPERTY_PREFIXES = Arrays.asList("", "jnlp.", "javaws.");

    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    @Override
    public List<String> getNormalisedProperties() {
        return System.getProperties().keySet().stream()
                .map(Object::toString)
                .map(x -> SYSTEM_PROPERTY_PREFIXES.stream()
                        .reduce(x, (memo, prefix) ->
                                memo.replaceFirst("^" + Pattern.quote(prefix), "")))
                .collect(Collectors.toList());
    }

    @Override
    public String getProperty(final String name) {
        checkArgument(StringUtils.isNotBlank(name));

        return SYSTEM_PROPERTY_PREFIXES.stream()
                .map(e -> System.getProperty(e + name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasProperty(String name) {
        return getPropertyEmptyAsNull(name) != null;
    }

    @Override
    public boolean getPropertyAsBoolean(final String name, final boolean defaultValue) {
        checkArgument(StringUtils.isNotBlank(name));

        return Optional.ofNullable(SYSTEM_PROPERTY_UTILS.getProperty(name))
                .map(String::toLowerCase)
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(Boolean::parseBoolean)
                .orElse(defaultValue);
    }

    @Override
    public float getPropertyAsFloat(final String name, final float defaultValue) {
        checkArgument(StringUtils.isNotBlank(name));

        return Optional.ofNullable(SYSTEM_PROPERTY_UTILS.getProperty(name))
                .map(String::toLowerCase)
                .map(String::trim)
                .map(NumberUtils::toFloat)
                .orElse(defaultValue);
    }

    @Override
    public int getPropertyAsInt(final String name, final int defaultValue) {
        checkArgument(StringUtils.isNotBlank(name));

        return Optional.ofNullable(SYSTEM_PROPERTY_UTILS.getProperty(name))
                .map(String::toLowerCase)
                .map(String::trim)
                .map(NumberUtils::toInt)
                .orElse(defaultValue);
    }

    @Override
    public long getPropertyAsLong(final String name, final long defaultValue) {
        checkArgument(StringUtils.isNotBlank(name));

        return Optional.ofNullable(SYSTEM_PROPERTY_UTILS.getProperty(name))
                .map(String::toLowerCase)
                .map(String::trim)
                .map(NumberUtils::toLong)
                .orElse(defaultValue);
    }

    @Override
    public String getPropertyEmptyAsNull(final String name) {
        checkArgument(StringUtils.isNotBlank(name));

        return SYSTEM_PROPERTY_PREFIXES.stream()
                .map(e -> System.getProperty(e + name))
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getPropertyNullAsEmpty(final String name) {
        checkArgument(StringUtils.isNotBlank(name));

        return SYSTEM_PROPERTY_PREFIXES.stream()
                .map(e -> System.getProperty(e + name))
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse("");
    }

    @Override
    public String getPropertyNullAsEmpty(final String name, final String append) {
        final String property = getPropertyNullAsEmpty(name);
        if (StringUtils.isEmpty(property)) {
            return property;
        }

        return property + append;
    }

    @Override
    public Optional<String> getPropertyAsOptional(final String name) {
        checkArgument(StringUtils.isNotBlank(name));

        return SYSTEM_PROPERTY_PREFIXES.stream()
                .map(e -> System.getProperty(e + name))
                .filter(StringUtils::isNotBlank)
                .findFirst();
    }

    public String getPropertiesAsCommandLineRags(final List<String> propertyNames) {
        return String.join(
                " ",
                propertyNames.stream()
                        .map(e -> "-D" + e + "=" + System.getProperty(e))
                        .collect(Collectors.toList())
        );
    }
}
