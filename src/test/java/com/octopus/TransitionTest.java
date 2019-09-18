package com.octopus;

import com.octopus.utils.impl.ScreenTransitionsImpl;
import org.junit.Test;

public class TransitionTest {
    @Test
    public void TestTransition() {
        new ScreenTransitionsImpl().fadeScreen(1, 0, 1, 2000);
    }
}
