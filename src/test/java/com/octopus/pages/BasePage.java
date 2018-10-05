package com.octopus.pages;

import com.octopus.AutomatedBrowser;

public class BasePage {

    protected static final int WAIT_TIME = 30;

    protected final AutomatedBrowser automatedBrowser;

    protected BasePage(AutomatedBrowser automatedBrowser) {
        this.automatedBrowser = automatedBrowser;
    }
}