package io.github.dylanrusselldev.utilities.screenrecorder;

import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.steps.Hooks;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.apache.commons.io.IOUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

/*
 * Filename: ScreenRecorderUtil.java
 * Author: Dylan Russell
 * Purpose: Enables the ability to record the desktop during test execution and save the file as an AVI file.
 *          Screen recording is not supported during headless and parallel execution.
 */
public class ScreenRecorderUtil extends ScreenRecorder {

    public static ScreenRecorder screenRecorder;
    private final String name;
    private static final LoggerClass LOGGER = new LoggerClass(ScreenRecorderUtil.class);

    public ScreenRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                              Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    /**
     * Create the file for the screen recorder.
     *
     * @param fileFormat the format of the file
     * @return the file object
     */
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {

        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

        return new File(movieFolder, name + "-" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
    }

    /**
     * Start the screen recorder.
     */
    public static void startRecord() throws Exception {

        if (RuntimeInfo.getThreads() > 1) {
            LOGGER.fail("Using the screen recorder during parallel execution has the " +
                    "chance of another browser window opening over the current test. Please run with -DThreads=1");
        }

        if (RuntimeInfo.isHeadless()) {
            LOGGER.fail("The screen recorder cannot be used while in Headless mode");
        }

        File file = new File(Constants.VIDEO_FOLDER_PATH);

        // Get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle rectangle = new Rectangle(0, 0, width, height);

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        // Build the screen recorder object
        screenRecorder = new ScreenRecorderUtil(gc, rectangle,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file, "main");

        screenRecorder.start();
        LOGGER.info("Started recording the screen");
    }

    /**
     * Stop the screen recorder.
     */
    public static void stopRecord() throws IOException {

        if (screenRecorder != null) {
            screenRecorder.stop();
            LOGGER.info("Stopped the screen recorder");
            CommonMethods.pauseForSeconds(1);
            AVItoMP4.convertAVIToMP4(CommonMethods.getNewestFile(Constants.VIDEO_FOLDER_PATH, "avi"));
            attachVideo();
            screenRecorder = null;
        } else {
            LOGGER.info("Screen recorder was not used");
        }

    }

    /**
     * Attach the screen recorder file to the execution report.
     */
    private static void attachVideo() throws IOException {
        FileInputStream fis = new FileInputStream(CommonMethods.getNewestFile(Constants.VIDEO_FOLDER_PATH, "mp4"));
        byte[] byteArr = IOUtils.toByteArray(fis);
        Hooks.getScenario().attach(byteArr, "video/mp4", "Click to view video");
    }

}
