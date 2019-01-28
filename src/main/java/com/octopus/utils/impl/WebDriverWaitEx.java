package com.octopus.utils.impl;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;

import java.time.Clock;
import java.time.Duration;

public class WebDriverWaitEx extends FluentWait<WebDriver> {
    public static final long DEFAULT_SLEEP_TIMEOUT = 500L;


    public WebDriverWaitEx(final WebDriver driver, final Duration time) {
        super(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER);
        this.withTimeout(time);
        this.pollingEvery(Duration.ofMillis(DEFAULT_SLEEP_TIMEOUT));
        this.ignoring(NotFoundException.class);
    }
}