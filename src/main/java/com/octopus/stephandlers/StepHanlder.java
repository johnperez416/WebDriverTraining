package com.octopus.stephandlers;

import io.cucumber.core.api.Scenario;

public interface StepHanlder {
    void handleStep(Scenario scenario);
}
