package net.$51zhiyuan.development.kidbridge.test.module;

import org.junit.Test;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    @Test
    public void test() {

//        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("X:/www.mp3"));
//        AudioFormat format = stream.getFormat();
//        Synthesizer synthesizer = MidiSystem.getSynthesizer();
//        synthesizer.open();
//        MidiChannel[] channels = synthesizer.getChannels();
//        channels[0].controlChange(7,1);

        String wavFile1 = "X:\\www.mp3";
        String wavFile2 = "X:\\test.mp3";

        try {
            File file1 = new File(wavFile1);
            File file2 = new File(wavFile2);
            AudioInputStream clip1 = AudioSystem.getAudioInputStream(file1);
            AudioInputStream clip2 = AudioSystem.getAudioInputStream(file2);


            AudioInputStream stream = AudioSystem
                    .getAudioInputStream(new File(wavFile2));
            AudioFormat format = stream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(), 16, format.getChannels(),
                        format.getChannels() * 2, format.getSampleRate(), false);
                stream = AudioSystem.getAudioInputStream(format, stream);
            }
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                    stream.getFormat());
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
                    .getLine(info);
            System.out.println(stream.getFrameLength());
//            sourceDataLine.open(stream.getFormat(), sourceDataLine.getBufferSize());
//            sourceDataLine.start();
//            int numRead = 0;
//            byte[] buf = new byte[sourceDataLine.getBufferSize()];
//            while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
//                int offset = 0;
//                while (offset < numRead) {
//                    offset += sourceDataLine.write(buf, offset, numRead - offset);
//                }
//                System.out.println(sourceDataLine.getFramePosition() + " "
//                        + sourceDataLine.getMicrosecondPosition());
//            }
//            sourceDataLine.drain();
//            sourceDataLine.stop();
//            sourceDataLine.close();
//            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
