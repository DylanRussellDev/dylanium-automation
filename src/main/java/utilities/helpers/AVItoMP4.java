/*
 * Filename: AVItoMP4.java
 * Purpose: Converts the .avi files generated from the methods
 *          in ScreenRecorderUtil.java to MP4 files. The MP4
 *          files can be attached to the test execution reports.
 */

package utilities.helpers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import utilities.core.CommonMethods;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

import static utilities.core.Constants.VIDEO_FILE_PATH;

public class AVItoMP4 {

    public static void convertAVIToMP4() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

        // Declare the AVI file to convert
        File aviFile = new File(String.valueOf(CommonMethods.getNewestFile(VIDEO_FILE_PATH, "avi")));

        // Declare where to put the converted MP4 file
        File target = new File(VIDEO_FILE_PATH + "\\result-video-" + dateFormat.format(new Date()) + ".mp4");

        // Set the Audio Attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(16000);    // Increase the value of the Bit Rate for higher quality audio
        audio.setChannels(2);

        // Set the Video Attributes
        VideoAttributes video = new VideoAttributes();
        video.setCodec("libx264");
        video.setBitRate(500000);   // Increase the value of the Bit Rate for higher quality video
        video.setFrameRate(15);
        video.setSize(new VideoSize(1280, 720));

        // Assign the attributes to the Encoding object
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        // Create a new multimedia object from the AVI file.
        MultimediaObject src = new MultimediaObject(aviFile);

        // Attempt to convert the AVI video to an MP4
        try {
            Encoder encoder = new Encoder();
            encoder.encode(src, target, attrs);
        } catch (Exception e) {
            throw new RuntimeException("Could not convert the file: " + aviFile
            + " to an MP4 file. \n Error Message: " + e.getMessage());
        } // end try catch

    } // end convertAVIToMP4()

 } // end AVItoMP4.java