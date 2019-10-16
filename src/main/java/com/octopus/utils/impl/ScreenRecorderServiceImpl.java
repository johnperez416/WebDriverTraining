package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.exceptions.VideoException;
import com.octopus.utils.ScreenRecorderService;
import com.octopus.utils.SystemPropertyUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.VideoFormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ScreenRecorderServiceImpl implements ScreenRecorderService {
    static final Logger LOGGER = Logger.getLogger(ScreenRecorderServiceImpl.class.toString());
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final int FPS = 15;
    private static final int MOUSE_FPS = 30;
    private static final int KEYFRAME_INTERVAL = 120;
    private static final int COLOUR_DEPTH = 16;
    private static final int MAX_RECORDING_TIME = 30000;
    private ScreenRecorder screenRecorder;

    @Override
    public void start(final File file) {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_VIDEO_RECORDING, false)) {
            return;
        }

        if (screenRecorder != null) {
            LOGGER.warning("The screen is already recording!");
            return;
        }

        try {
            LOGGER.info("Starting video recording");

            // set the graphics configuration
            final GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            screenRecorder = new org.monte.screenrecorder.ScreenRecorder(gc,
                    gc.getBounds(),
                    new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.FILE, FormatKeys.MimeTypeKey, FormatKeys.MIME_AVI),
                    new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            VideoFormatKeys.CompressorNameKey, VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            VideoFormatKeys.DepthKey, COLOUR_DEPTH,
                            VideoFormatKeys.FrameRateKey, Rational.valueOf(FPS),
                            VideoFormatKeys.QualityKey, 1.0f,
                            VideoFormatKeys.KeyFrameIntervalKey, KEYFRAME_INTERVAL),
                    new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, "black", FormatKeys.FrameRateKey, Rational.valueOf(MOUSE_FPS)),
                    null,
                    file);
            screenRecorder.setMaxRecordingTime(MAX_RECORDING_TIME);
            screenRecorder.start();
        } catch (final Exception ex) {
            throw new VideoException("Failed to set up screen recording", ex);
        }
    }

    @Override
    public void stop() {
        try {
            if (screenRecorder != null) {
                LOGGER.info("Stopping video recording");
                screenRecorder.stop();
            }
            screenRecorder = null;
        } catch (final IOException ex) {
            throw new VideoException("Failed to stop screen recording", ex);
        }
    }
}
