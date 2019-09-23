package com.octopus.utils.impl;

import com.octopus.utils.ScreenTransitions;

import javax.swing.*;
import java.awt.*;

public class ScreenTransitionsImpl implements ScreenTransitions {
    private static JFrame frame = null;

    @Override
    public void clear() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
        frame = null;
    }

    @Override
    public void fadeScreen(final float red, final float green, final float blue, final long transitionTime) {
        if (frame == null) {
            frame = new JFrame();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setAlwaysOnTop(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        frame.setBackground(new Color(red, green, blue, 0.0f));

        final long start = System.currentTimeMillis();
        while (true) {
            final long now = System.currentTimeMillis();
            if (now >= start + transitionTime) {
                break;
            }
            frame.setBackground(new Color(red, green, blue, (now - start) / (float)transitionTime));
        }
    }
}
