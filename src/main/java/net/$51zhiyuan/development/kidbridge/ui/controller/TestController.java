package net.$51zhiyuan.development.kidbridge.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

//    @Autowired
//    private PushService pushService;
//
//    @ResponseBody
//    @RequestMapping("/audio")
//    Object audio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
//        AudioInputStream stream = AudioSystem
//                .getAudioInputStream(new File("X:\\www.mp3"));
//        AudioFormat format = stream.getFormat();
//        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
//            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
//                    format.getSampleRate(), 16, format.getChannels(),
//                    format.getChannels() * 2, format.getSampleRate(), false);
//            stream = AudioSystem.getAudioInputStream(format, stream);
//        }
//        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
//                stream.getFormat());
//        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
//                .getLine(info);
//        sourceDataLine.open(stream.getFormat(), sourceDataLine.getBufferSize());
//        sourceDataLine.start();
//        int numRead = 0;
//        byte[] buf = new byte[sourceDataLine.getBufferSize()];
//        while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
//            int offset = 0;
//            while (offset < numRead) {
//                offset += sourceDataLine.write(buf, offset, numRead - offset);
//            }
//            System.out.println(sourceDataLine.getFramePosition() + " "
//                    + sourceDataLine.getMicrosecondPosition());
//        }
//        sourceDataLine.drain();
//        sourceDataLine.stop();
//        sourceDataLine.close();
//        stream.close();
//        return "success";
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/push", produces = MediaType.APPLICATION_JSON_VALUE)
//    Object push() throws APIConnectionException, APIRequestException, JsonProcessingException {
//        SystemPush systemPush = new SystemPush();
//        systemPush.setMessage("hello");
//        this.pushService.sendSystemPush(14,systemPush);
//        return "";
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/push/queue", produces = MediaType.APPLICATION_JSON_VALUE)
//    Object push_queue() throws APIConnectionException, APIRequestException, JsonProcessingException {
//        this.pushService.sendQueue(14);
//        return "";
//    }

}
