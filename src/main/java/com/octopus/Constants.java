package com.octopus;

public class Constants {
    public static final String ALIAS_HEADER_PREFIX = "CucumberAlias-";

    /**
     * The system property that can be used to enable the mouse cursor to be moved
     * to the element being interacted with
     */
    public static final String MOVE_CURSOR_TO_ELEMENT = "moveCursorToElement";

    /**
     * The system property that can be used to disable any element highlighting steps
     */
    public static final String DISABLE_HIGHLIGHTS = "disableHighlights";

    /**
     * The system property that can be used to disable any video recording
     */
    public static final String DISABLE_VIDEO_RECORDING = "disableVideoRecording";

    /**
     * The system property that can be used to disable any screenshots
     */
    public static final String DISABLE_SCREENSHOTS = "disableScreenshots";

    /**
     * A value to add to the vertical position of elements to account for message bars
     * like the one in chrome when being run as part of an automated test
     */
    public static final String MOUSE_MOVE_VERTICAL_OFFSET = "mouseMoveVerticalOffset";

    /**
     * The system property that can be used in conjunction with moveCursorToElement
     * to define the screen zoom in windows.
     */
    public static final String SCREEN_ZOOM_FACTOR = "screenZoomFactor";

    /**
     * The amount of time to take moving the mouse across the screen
     */
    public static final int MOUSE_MOVE_TIME = 500;
    /**
     * The number of steps to use when moving the mouse across the screen
     */
    public static final int MOUSE_MOVE_STEPS = 100;
}
