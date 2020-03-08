package com.octopus.stephandlers.impl;

import com.octopus.Constants;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepFinished;
import io.vavr.control.Try;

/**
 * A step handler to add a pause between each interaction step.
 */
public class StepPauseHandler implements EventListener {

    /**
     * Prefixes used for any steps that simulate user interaction.
     */
    private static final String[] PREFIXES = new String[]{"I ", "I force "};
    /**
     * Action keywords that indicate steps that simulate user interaction.
     */
    private static final String[] ACTION_KEYWORDS = new String[]{"click", "populate", "clear", "select", "scroll", "zoom", "mouse over", "focus", "press"};
    /**
     * The shared SystemPropertyUtilsImpl instance.
     */
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    /**
     * How long to pause after a simulated user interaction.
     */
    private final int pauseTime;

    /**
     * Default constructor.
     */
    public StepPauseHandler() {
        pauseTime = SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.STEP_PAUSE, 0);
    }

    private void handleTestStepFinished(final TestStepFinished event) {
        if (pauseTime > 0 && isActionStep(event)) {
            Try.run(() -> Thread.sleep(pauseTime));
        }
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    /**
     * Match any action steps.
     *
     * @param event The event
     * @return true if it is an action step, and false otherwise
     */
    private boolean isActionStep(final TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            final String step =  ((PickleStepTestStep) event.getTestStep()).getStep().getText();
            for (String prefix : PREFIXES) {
                for (String actionKeyword : ACTION_KEYWORDS) {
                    if (step.startsWith(prefix + actionKeyword)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
