package com.octopus.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

@FunctionalInterface
public interface ExpectedConditionCallback {
    ExpectedCondition<WebElement> getExpectedCondition(By by);
}