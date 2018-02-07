package net.$51zhiyuan.development.kidbridge.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 常用工具包
 */
public class Util {


    private static final KidbridgeObjectMapper OBJECT_MAPPER = new KidbridgeObjectMapper();
    private static final Random RANDOM = new Random();

    public static String sign(SortedMap<String, Object> sortedMap, String salt) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> map : sortedMap.entrySet()) {
            if (map.getValue() == null || StringUtils.isBlank(map.getValue().toString())) {
                continue;
            }
            stringBuilder.append(String.format("%s=%s&", map.getKey(),((map.getValue() instanceof Number || map.getValue() instanceof String) ? map.getValue() : OBJECT_MAPPER.writeValueAsString(map.getValue()))));
        }

        stringBuilder.append(String.format("%s=%s", "salt", salt));
        String md5 = DigestUtils.md5Hex(stringBuilder.toString()).toLowerCase();
        return md5;
    }

    public static String sign(SortedMap<String, Object> sortedMap) throws JsonProcessingException {
        return sign(sortedMap,Configuration.SYSTEM_SIGN_SALT);
    }

    public static String sign(Map map) throws JsonProcessingException {
        return sign(new TreeMap(map),Configuration.SYSTEM_SIGN_SALT);
    }

    public static String subString(String targetString, int byteIndex) throws Exception {
        if (targetString.getBytes("UTF-8").length < byteIndex) {
            return targetString;
        }
        String temp = targetString;
        for (int i = 0; i < targetString.length(); i++) {
            if (temp.getBytes("UTF-8").length <= byteIndex) {
                break;
            }
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp + "...";
    }

    public static Double nextDouble(final double min, final double max) {
        if (max < min) {
            throw new RuntimeException("min < max");
        }
        if (min == max) {
            return min;
        }
        return min + ((max - min) * RANDOM.nextDouble());
    }

    public static Integer nextInt(final int min, final int max) {
        return nextDouble(min, max).intValue();
    }

    public static String generateString(int length, boolean randomToUpperCase) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] charArr = new char[]{'0', 'a', 'b', 'c', '1', 'd', 'e', 'f', '2', 'g', 'h', 'i', '3', 'j', 'k', 'l', '4', 'm', 'n', 'o', '5', 'p', 'q', 'r', '6', 's', 't', 'u', '7', 'v', 'w', 'x', '8', 'y', 'z', '9'};
        for (int i = 0; i < length; i++) {
            int r = RANDOM.nextInt(charArr.length);
            char s = charArr[r];
            if (randomToUpperCase) {
                int c = RANDOM.nextInt(charArr.length) + 1;
                if (c % 2 == 0) {
                    s = Character.toUpperCase(s);
                }
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static String generateString(int length) {
        return generateString(length, false);
    }

    public static String dictionarySort(String... params) {
        Arrays.sort(params);
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : params) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public static String sign(String... params) {
        Arrays.sort(params);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            stringBuffer.append(params[i]);
            if (i + 1 < params.length) {
                stringBuffer.append("&");
            }
        }
        return stringBuffer.toString();
    }

    public static BufferedImage generateQRCode(int qrSize, String qrText) throws WriterException {
        return Util.generateQRCode(qrSize, qrText, null);
    }

    public static BufferedImage generateQRCode(int qrSize, String qrText, String qrDescribe) throws WriterException {
        Map hints = new Hashtable();
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);
        BufferedImage bitMatrixBufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage bufferedImage = new BufferedImage(qrSize, qrSize + (StringUtils.isBlank(qrDescribe) ? 0 : 40), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.drawImage(bitMatrixBufferedImage, 0, 0, null);
        g.setBackground(Color.WHITE);
        g.clearRect(0, qrSize, qrSize, qrSize);
        if (!StringUtils.isBlank(qrDescribe)) {
            g.setFont(new Font(null, Font.BOLD, 15));
            g.setColor(Color.BLACK);
            int strWidth = g.getFontMetrics().stringWidth(qrDescribe);
            g.drawString(qrDescribe, qrSize / 2 - strWidth / 2, qrSize + 25); //画文字
        }
        Color c = new Color(220, 220, 220);
        g.setColor(c);
        g.drawLine(0, 0, qrSize, 0);
        g.drawLine(0, qrSize, qrSize, qrSize);
        g.drawLine(0, 0, 0, qrSize);
        g.drawLine(qrSize - 1, 0, qrSize - 1, qrSize);
        g.dispose();
        return bufferedImage;
    }

    public static Long generateID(Long salt) {
        Calendar curDate = Calendar.getInstance();
        long second = curDate.get(Calendar.DAY_OF_MONTH) * 24 * 60 * 60 + ((curDate.get(Calendar.HOUR_OF_DAY) * 3600) + (curDate.get(Calendar.MINUTE) * 60) + (curDate.get(Calendar.SECOND)));
        int y = curDate.get(Calendar.YEAR);
        int m = curDate.get(Calendar.MONTH) + 1;
        int ten = y / 10 % 10;
        int unit = y / 1 % 10;
        salt = ((salt / 100 % 10) * 100) + ((salt / 10 % 10) * 10) + ((salt / 1 % 10));
        String result = String.format("%08d%03d", (((ten * 10 + unit) * 10000000) + (m * 1000000 + second)), salt);
        return Long.valueOf(result);
    }

    public static void printJsonMessage(HttpServletResponse response, Object message) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(new KidbridgeObjectMapper().writeValueAsString(message));
        printWriter.flush();
        printWriter.close();
        response.flushBuffer();
    }

}
