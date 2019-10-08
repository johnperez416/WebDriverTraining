package com.octopus.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Defines a service that can be used to move the mouse on the screen
 */
public interface MouseMovementUtils {
	/**
	 *
	 * @param x1 Start x
	 * @param y1 Start y
	 * @param x2 End x
	 * @param y2 End y
	 * @param time The amount of time to take
	 * @param steps The number of steps to use
	 */
	void mouseGlide(int x1, int y1, int x2, int y2, int time, int steps);

	/**
	 * Move the mouse from the current position to the new position
	 * @param x2 End x
	 * @param y2 End y
	 * @param time The amount of time to take
	 * @param steps The number of steps to use
	 */
	void mouseGlide(int x2, int y2, int time, int steps);

	/**
	 * Move the mouse from the current position to the new position
	 * @param driver The WebDriver instance
	 * @param javascriptExecutor The selenium javascript executor
	 * @param element the callback to get the element
	 * @param time The amount of time to take
	 * @param steps The number of steps to use
	 * @param force true if we are forcing interaction on the element
	 */
	void mouseGlide(WebDriver driver,
                    JavascriptExecutor javascriptExecutor,
					GetElement element,
                    int time,
                    int steps,
					boolean force);

	/**
	 * Move the mouse from the current position to the new position
	 * @param driver The WebDriver instance
	 * @param javascriptExecutor The selenium javascript executor
	 * @param element the callback to get the element
	 * @param time The amount of time to take
	 * @param steps The number of steps to use
	 */
	default void mouseGlide(WebDriver driver,
					JavascriptExecutor javascriptExecutor,
					GetElement element,
					int time,
					int steps) {mouseGlide(driver, javascriptExecutor, element, time, steps, false);}
}
