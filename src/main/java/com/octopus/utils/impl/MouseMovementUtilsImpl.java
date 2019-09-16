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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of MouseMovementUtils
 */
public class MouseMovementUtilsImpl implements MouseMovementUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(MouseMovementUtilsImpl.class);

	private SystemPropertyUtils systemPropertyUtils = new SystemPropertyUtilsImpl();

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
		final Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		mouseGlide(mouseLocation.x, mouseLocation.y, x2, y2, time, steps);
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
			systemPropertyUtils.getPropertyAsBoolean(
				Constants.MOVE_CURSOR_TO_ELEMENT, false);

		final int verticalOffset =
			systemPropertyUtils.getPropertyAsInt(
				Constants.MOUSE_MOVE_VERTICAL_OFFSET, 0);

		if (moveMouseCursor) {

			final float zoom = systemPropertyUtils.getPropertyAsFloat(
				Constants.SCREEN_ZOOM_FACTOR, 1.0f);

			final Long top = (Long) javascriptExecutor.executeScript(
					"return Math.floor(arguments[0].getBoundingClientRect().top);", element);
			final Long left = (Long) javascriptExecutor.executeScript(
					"return Math.floor(arguments[0].getBoundingClientRect().left);", element);
			final Long height = (Long) javascriptExecutor.executeScript(
				"return arguments[0].clientHeight;", element);
			final Long width = (Long) javascriptExecutor.executeScript(
				"return arguments[0].clientWidth;", element);
			mouseGlide(
				(int) ((left + width / 2) * zoom),
				(int) ((top + verticalOffset + height / 2) * zoom),
				Constants.MOUSE_MOVE_TIME,
				Constants.MOUSE_MOVE_STEPS);

			new Actions(driver).moveToElement(element).perform();
		}
	}
}
