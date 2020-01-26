package com.octopus.stephandlers;

import com.octopus.Constants;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestStepFinished;
import io.vavr.control.Try;

/**
 * A handler to add a pause between each step
 */
public class StepPauseHandler implements EventListener {

    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private final int pauseTime;

    public StepPauseHandler() {
        pauseTime = SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.STEP_PAUSE, 0);
    }

    private void handleTestStepFinished(final TestStepFinished event) {
        if (pauseTime > 0) {
            Try.run(() -> Thread.sleep(pauseTime * 1000));
        }
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }
}
