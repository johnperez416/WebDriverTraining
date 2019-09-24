package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.utils.MouseMovementUtils;
import com.octopus.utils.SystemPropertyUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of MouseMovementUtils
 */
public class MouseMovementUtilsImpl implements MouseMovementUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MouseMovementUtilsImpl.class);

    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    @Override
    public void mouseGlide(final int x1, final int y1, final int x2, final int y2, final int time, final int steps) {
        try {
            final Robot r = new Robot();

            final double dx = (x2 - x1) / ((double) steps);
            final double dy = (y2 - y1) / ((double) steps);
            final double dt = time / ((double) steps);
            for (int step = 1; step <= steps; step++) {
                Thread.sleep((int) dt);
                r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
            }
        } catch (final AWTException | InterruptedException ex) {
            LOGGER.error("WEBAPPTESTER-BUG-0010: Exception thrown while moving mouse cursor", ex);
        }
    }

    @Override
    public void mouseGlide(final int x2, final int y2, final int time, final int steps) {
        Optional.ofNullable(MouseInfo.getPointerInfo())
                .map(x -> x.getLocation())
                .ifPresent(l -> mouseGlide(l.x, l.y, x2, y2, time, steps));
    }

    @Override
    public void mouseGlide(
            final WebDriver driver,
            final JavascriptExecutor javascriptExecutor,
            final WebElement element,
            final int time,
            final int steps) {

        checkNotNull(element);

        final boolean moveMouseCursor =
                SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(
                        Constants.MOVE_CURSOR_TO_ELEMENT, false);

        final int verticalOffset =
                SYSTEM_PROPERTY_UTILS.getPropertyAsInt(
                        Constants.MOUSE_MOVE_VERTICAL_OFFSET, 0);

        if (moveMouseCursor) {

            final float zoom = SYSTEM_PROPERTY_UTILS.getPropertyAsFloat(
                    Constants.SCREEN_ZOOM_FACTOR, 1.0f);

            final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

            final Long top = (Long) javascriptExecutor.executeScript(
                    "return Math.floor(arguments[0].getBoundingClientRect().top);", element);
            final Long left = (Long) javascriptExecutor.executeScript(
                    "return Math.floor(arguments[0].getBoundingClientRect().left);", element);
            final Long height = (Long) javascriptExecutor.executeScript(
                    "return arguments[0].clientHeight;", element);
            final Long width = (Long) javascriptExecutor.executeScript(
                    "return arguments[0].clientWidth;", element);
            mouseGlide(
                    Math.min(d.width - 1, (int) ((left + width / 2) * zoom)),
                    Math.min(d.height - 1, (int) ((top + verticalOffset + height / 2) * zoom)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            new Actions(driver).moveToElement(element).perform();
        }
    }
}
