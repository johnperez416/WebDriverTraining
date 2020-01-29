package com.octopus;

import java.util.ArrayList;

public class Constants {
    public static final ArrayList<String> DEFAULT_CUCUMBER_OPTIONS = new ArrayList<>() {{
        add("--glue");
        add("com.octopus.decoratorbase");
        add("--plugin");
        add("com.octopus.stephandlers.impl.SlackStepHandler");
        add("--plugin");
        add("com.octopus.stephandlers.impl.StepPauseHandler");
        add("--strict");
    }};

    /**
     * The amount of time to pause between each step
     */
    public static final String STEP_PAUSE = "stepPause";

    /**
     * Show the state of various options enabling and disabling features at startup
     */
    public static final String DUMP_OPTIONS = "dumpOptions";

    public static final String ALIAS_HEADER_PREFIX = "CucumberAlias-";

    /**
     * The prefix for any messages sent out from event handlers
     */
    public static final String STEP_HANDLER_MESSAGE = "stepHandlerMessage";

    /**
     * The number of times to retry a feature
     */
    public static final String RETRY_COUNT = "retryCount";
    /**
     * The delay before the next retry
     */
    public static final int RETRY_DELAY = 5000;
    /**
     * Automatically clean up the browser on shutdown
     */
    public static final String BROWSER_CLEANUP = "browserAutomaticCleanup";

    /**
     * If a scenario fails, setting this property to true means the aliases will be dumped.
     * This is useful for checking that a step referenced the expected alias.
     */
    public static final String DUMP_ALIASES_ON_FAILURE = "dumpAliasesOnFailure";

    /**
     * A list of addresses to exclude from the browser proxy
     */
    public static final String NO_PROXY_LIST = "noProxyList";

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
     * The system property that can be used to disable any Octopus service messages
     */
    public static final String DISABLE_SERVICEMESSAGES = "disableOctopusServiceMessages";

    /**
     * The system property that can be used to disable any video recording
     */
    public static final String DISABLE_VIDEO_RECORDING = "disableVideoRecording";

    /**
     * The system property that can be used to disable any screenshots
     */
    public static final String DISABLE_SCREENSHOTS = "disableScreenshots";

    /**
     * The system property that can be used to disable any browser zooming
     */
    public static final String DISABLE_ZOOM = "disableBrowserZoom";

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

    /**
     * The system property defining the number of reties to make when sending messages to slack
     */
    public static final String SLACK_RETRIES = "slackRetries";
    /**
     * The default slack retry count
     */
    public static final int DEFAULT_SLACK_RETRIES = 10;
    /**
     * The system property defining the backoff between slack api requests
     */
    public static final String SLACK_BACKOFF = "slackBackoff";
    /**
     * The default slack backoff period
     */
    public static final int DEFAULT_SLACK_BACKOFF = 2000;
    /**
     * The system property setting the maximum time for an individual screen recording video file. Smaller values
     * save memory.
     */
    public static final String SCREEN_RECORDING_MAX_TIME = "screenRecordingMaxTime";
    /**
     * Limit video files to 30 seconds by default
     */
    public static final long DEFAULT_SCREEN_RECORDING_MAX_TIME = 3600000L;
    /**
     * The system property that defines the screen recording fps
     */
    public static final String SCREEN_RECORDING_FPS = "screenRecordingFps";
    /**
     * The default screen recording FPS
     */
    public static final int DEFAULT_SCREEN_RECORDING_FPS = 15;
    /**
     * The system property that defines the screen recording mouse fps
     */
    public static final String SCREEN_RECORDING_MOUSE_FPS = "screenRecordingMouseFps";
    /**
     * The default screen recording mouse FPS
     */
    public static final int DEFAULT_SCREEN_RECORDING_MOUSE_FPS = 30;
    /**
     * The system property that defines the screen recording keyframe interval
     */
    public static final String SCREEN_RECORDING_KEYFRAME_INTERVAL = "screenRecordingKeyframeInterval";
    /**
     * The default screen recording keyframe interval
     */
    public static final int DEFAULT_SCREEN_RECORDING_KEYFRAME_INTERVAL = 120;
    /**
     * The system property that defines the screen recording colour depth
     */
    public static final String SCREEN_RECORDING_COLOUR_DEPTH = "screenRecordingColourDepth";
    /**
     * The default screen recording colour depth
     */
    public static final int DEFAULT_SCREEN_RECORDING_COLOUR_DEPTH = 16;
    /**
     * The default amount of time to delay simulated keystrokes
     */
    public static final int DEFAULT_INPUT_DELAY = 100;

    /**
     * The system property that can be set to save screenshots of elements that were matched
     * by the "simple by" lookup
     */
    public static final String SAVE_SCREENSHOTS_OF_MATCHED_ELEMENTS = "saveScreenshotsOfMatchedMethods";
}
