/*
 * Filename: ScreenRecorderUtil.java
 * Purpose: Enables the ability to record the desktop during test execution and save the file as an AVI file.
 *          In the stopRecord method, the AVI file is converted toan MP4 for easy viewing on any system and
 *          to also embed in the test execution report.
 *          Please note that screen recording won't capture browser actions while executing in Headless mode.
 */

package utilities.helpers;

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

import org.apache.commons.io.IOUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import utilities.core.CommonMethods;
import utilities.core.Hooks;

import static utilities.core.Constants.VIDEO_FILE_PATH;

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

public class ScreenRecorderUtil extends ScreenRecorder {

    public static ScreenRecorder screenRecorder;
    public String name;

    public ScreenRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                              Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    } // end constructor

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {

        // Create the directory to put the AVI file in
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        } // end if else

        // Create the video file name using date and time for clarity
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

        return new File(movieFolder, name + "-" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
    } // end createMovieFile()

    public static void startRecord(String methodName) throws Exception {
        if (Hooks.headless.equalsIgnoreCase("false")) {
            File file = new File(VIDEO_FILE_PATH);

            // Get the size of the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width;
            int height = screenSize.height;

            Rectangle captureSize = new Rectangle(0, 0, width, height);

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice()
                    .getDefaultConfiguration();

            screenRecorder = new ScreenRecorderUtil(gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                            Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),

                    null, file, methodName);

            // Start the recording
            screenRecorder.start();
        } else {
            System.out.println("Unable to record screen while executing in headless mode. Continuing execution...");
        } // end if else
    } // end startRecord()

    public static void stopRecord() throws Exception {
        // If executing in Headless mode,
        if (Hooks.headless.equalsIgnoreCase("false")) {
            screenRecorder.stop();
            CommonMethods.pauseForSeconds(1);
            AVItoMP4.convertAVIToMP4();
            attachVideo();
        } else {
            System.out.println("Unable to record screen while executing in headless mode. Continuing Execution...");
        } // end if else
    } // end stopRecord()

    private static void attachVideo() throws IOException {
        FileInputStream is = new FileInputStream(CommonMethods.getNewestFile(VIDEO_FILE_PATH, "mp4"));
        byte[] byteArr = IOUtils.toByteArray(is);
        Hooks.scenario.get().attach(byteArr, "video/mp4", "Click to view video");
    } // end attachVideo()

} // end ScreenRecorderUtil