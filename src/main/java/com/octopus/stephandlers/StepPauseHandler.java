package com.octopus.stephandlers;

import com.octopus.Constants;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
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
        if (pauseTime > 0 && isActionStep(event)) {
            Try.run(() -> Thread.sleep(pauseTime * 1000));
        }
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    /**
     * Match any action steps
     * @param event The evenmt
     * @return true if it is an action step, and false otherwise
     */
    private boolean isActionStep(final TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            final String step =  ((PickleStepTestStep) event.getTestStep()).getStep().getText();
            return step.contains("click") ||
                    step.contains("populate") ||
                    step.contains("clear") ||
                    step.contains("select") ||
                    step.contains("scroll") ||
                    step.contains("zoom") ||
                    step.contains("mouse over") ||
                    step.contains("focus") ||
                    step.contains("press");
        }

        return false;
    }
}
