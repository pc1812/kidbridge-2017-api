package net.$51zhiyuan.development.kidbridge.test.module;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Word {

    @Test
    public void word() throws IOException {
        String path = "X:\\t.docx";
        InputStream inputStream = new FileInputStream(path);
        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
        XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(xwpfDocument);
        System.out.println(xwpfWordExtractor.getText());
    }
}
