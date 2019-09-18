package com.octopus;

import com.octopus.utils.ScreenTransitions;
import com.octopus.utils.impl.ScreenTransitionsImpl;
import org.junit.Test;

public class TransitionTest {
    private static final ScreenTransitions SCREEN_TRANSITIONS =  new ScreenTransitionsImpl();
    @Test
    public void TestTransition() {
        SCREEN_TRANSITIONS.fadeScreen(1, 0, 1, 2000);
        SCREEN_TRANSITIONS.clear();
    }
}
