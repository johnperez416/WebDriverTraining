package com.octopus.stephandlers;

import io.cucumber.core.api.Scenario;

public interface StepHandler {
    String STEP_HANDLER_MESSAGE = "stepHandlerMessage";
    void handleStep(Scenario scenario);
}
