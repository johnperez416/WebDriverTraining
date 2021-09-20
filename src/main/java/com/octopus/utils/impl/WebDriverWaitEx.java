package com.octopus.utils.impl;

import org.openqa.selenium.WebDriver;
import java.time.Clock;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WebDriverWaitEx extends WebDriverWait {
    public static final long DEFAULT_SLEEP_TIMEOUT = 10;

    public WebDriverWaitEx(final WebDriver driver, final long timeOutInSeconds) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, DEFAULT_SLEEP_TIMEOUT);
    }

    public WebDriverWaitEx(final WebDriver driver, final long timeOut, final TimeUnit time) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOut, DEFAULT_SLEEP_TIMEOUT, time);
    }

    public WebDriverWaitEx(final WebDriver driver, final long timeOutInSeconds, final long sleepInMillis) {
        this(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, sleepInMillis);
    }

    public WebDriverWaitEx(
            final WebDriver driver,
            final Clock clock,
            final Sleeper sleeper,
            final long timeOutInSeconds,
            final long sleepTimeOut) {

        this(driver, clock, sleeper, timeOutInSeconds, sleepTimeOut, TimeUnit.SECONDS);
    }

    public WebDriverWaitEx(
            final WebDriver driver,
            final Clock clock,
            final Sleeper sleeper,
            final long timeOut,
            final long sleepTimeOut,
            final TimeUnit time) {

        // Call the WebDriverWait constructor with a timeout of 0
        super(driver, Duration.of(timeOut, time.toChronoUnit()), Duration.ofSeconds(sleepTimeOut), clock, sleeper);
    }
}