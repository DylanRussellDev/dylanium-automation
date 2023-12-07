package io.github.dylanrusselldev.utilities.screenrecorder;

import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Filename: AVItoMP4.java
 * Author: Dylan Russell
 * Purpose: Converts the .avi files generated from the methods in ScreenRecorderUtil.java to MP4 files.
 */
public class AVItoMP4 {

    private static final LoggerClass LOGGER = new LoggerClass(AVItoMP4.class);

    /**
     * Convert an AVI file to an MP4 to attach to the reports.
     */
    public static void convertAVIToMP4(File file) {
        LOGGER.info("Attempting to convert the AVI file to MP4");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

        // Output directory for MP4
        File target = new File(Constants.VIDEO_FOLDER_PATH + "\\result-video-" + sdf.format(new Date()) + ".mp4");

        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec("libmp3lame");
        audioAttributes.setBitRate(16000);    // Increase the value of the Bit Rate for higher quality audio
        audioAttributes.setChannels(2);

        VideoAttributes videoAttributes = new VideoAttributes();
        videoAttributes.setCodec("libx264");
        videoAttributes.setBitRate(500000);   // Increase the value of the Bit Rate for higher quality video
        videoAttributes.setFrameRate(15);
        videoAttributes.setSize(new VideoSize(1920, 1080));

        EncodingAttributes encodingAttributes = new EncodingAttributes();
        encodingAttributes.setOutputFormat("mp4");
        encodingAttributes.setAudioAttributes(audioAttributes);
        encodingAttributes.setVideoAttributes(videoAttributes);

        MultimediaObject multimediaObject = new MultimediaObject(file);

        try {
            Encoder encoder = new Encoder();
            encoder.encode(multimediaObject, target, encodingAttributes);
        } catch (Exception e) {
            LOGGER.error("Could not convert the file: " + file + " to an MP4 file.", e);
        }

    }

}
