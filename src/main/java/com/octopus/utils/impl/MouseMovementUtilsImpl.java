package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.utils.GetElement;
import com.octopus.utils.MouseMovementUtils;
import com.octopus.utils.RetryService;
import com.octopus.utils.SystemPropertyUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
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
    private static final RetryService RETRY_SERVICE = new RetryServiceImpl();

    @Override
    public void mouseGlide(final int x1, final int y1, final int x2, final int y2, final int time, final int steps) {
        try {
            final Robot r = new Robot();

            final double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            final double fixedSteps = Math.min(dist, steps);

            final double dx = (x2 - x1) / (fixedSteps);
            final double dy = (y2 - y1) / (fixedSteps);
            final double dt = time / (fixedSteps);
            for (int step = 1; step <= fixedSteps; step++) {
                Thread.sleep((int) dt);
                r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
            }
        } catch (final AWTException | InterruptedException ex) {
            LOGGER.error("Exception thrown while moving mouse cursor", ex);
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
            final GetElement element,
            final int time,
            final int steps,
            final boolean force) {

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

            /*
                This can fail with the error:
                The element reference of <reference> is stale; either the element is no longer attached to the DOM, it is not in the current frame context, or the document has been refreshed.
                We retry here to allow the movement to complete if the source element disappears.
             */
            final Long[] rect = RETRY_SERVICE.getTemplate()
                    .execute(context -> {
                        final WebElement webElement = element.getElement();
                        final Long top = (Long) javascriptExecutor.executeScript(
                                "return Math.floor(arguments[0].getBoundingClientRect().top);", webElement);
                        final Long left = (Long) javascriptExecutor.executeScript(
                                "return Math.floor(arguments[0].getBoundingClientRect().left);", webElement);
                        final Long height = (Long) javascriptExecutor.executeScript(
                                "return Math.floor(arguments[0].getBoundingClientRect().height);", webElement);
                        final Long width = (Long) javascriptExecutor.executeScript(
                                "return Math.floor(arguments[0].getBoundingClientRect().width);", webElement);
                        return new Long[]{left, top, width, height};
                    });

            mouseGlide(
                    Math.min(d.width - 1, (int) ((rect[0] + rect[2] / 2) * zoom)),
                    Math.min(d.height - 1, (int) ((rect[1] + verticalOffset + rect[3] / 2) * zoom)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            /*
                This can fail with the error:
                The element reference of <reference> is stale; either the element is no longer attached to the DOM, it is not in the current frame context, or the document has been refreshed.
                We retry here to allow the movement to complete if the source element disappears.
             */
            try {
                RETRY_SERVICE.getTemplate()
                        .execute(context -> {
                            new Actions(driver).moveToElement(element.getElement()).perform();
                            return 1;
                        });
            } catch (final MoveTargetOutOfBoundsException ex) {
                /*
                    We may be forcing a click on an element specifically to work around the fact that it is not
                    something Selenium can reach. In this case, ignore the out of bounds exeception.
                 */
                if (!force) {
                    throw ex;
                }
            } catch (final WebDriverException ex) {
                /*
                    Working with hidden elements (a common example here is a hidden file input type) means there is
                    nothing to move the mouse to, so we ignore it and move on.
                 */
                if (!ex.getMessage().contains("rect is undefined")) {
                    throw ex;
                }
            }

        }
    }
}
