package net.$51zhiyuan.development.kidbridge.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.ui.model.Book;
import net.$51zhiyuan.development.kidbridge.ui.model.BookSegment;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hkhl.cn on 2018/3/7.
 */
public class ImportAudio {

    @Test
    public void audio() throws IOException {
        List<String> waring = new ArrayList<>();
        List<String> error = new ArrayList<>();
        File path = new File("E:\\data\\apache\\tomcat-temp-a\\temp\\kidbridge");
        for(File dir : path.listFiles()){
            try{
                File bookWordsPath = null; // 文字稿word文档路径
                List<File> bookAudio = null; // 段落音频资源列表
                // 遍历解压出来的资源
                for(File file : dir.listFiles()){
                    if(file.getName().indexOf("文字") > -1){ // 处理“文字”文件夹资源
                        bookWordsPath = file;
                    }
                    if(file.getName().indexOf("音频") > -1){ // 处理“音频”文件夹资源
                        bookAudio = Arrays.asList(file.listFiles());
                        // 资源排序
                        Collections.sort(bookAudio,new Comparator<File>() {
                            @Override
                            public int compare(File o1, File o2) {
                                int int1 = 0;
                                int int2 = 0;
                                Matcher matcher1 = Pattern.compile("(\\d+)\\.\\w+$").matcher(o1.getName());
                                Matcher matcher2 = Pattern.compile("(\\d+)\\.\\w+$").matcher(o2.getName());
                                if(matcher1.find()){
                                    int1 = Integer.valueOf(matcher1.group(1));
                                }
                                if(matcher2.find()){
                                    int2 = Integer.valueOf(matcher2.group(1));
                                }
                                return int1 > int2 ? 1 : -1;
                            }
                        });
                    }
                }

                File bookWordsFile = bookWordsPath.listFiles()[0]; // 文字稿word文档资源，只取第一个文件
                // word文档解析处理
                InputStream inputStream = new FileInputStream(bookWordsFile.getPath());
                XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
                XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(xwpfDocument);
                String bookWordsBody = xwpfWordExtractor.getText(); // 获取word文档纯文本内容
                Book book = new Book();
                Matcher name = Pattern.compile("1，(.*?)\\n").matcher(bookWordsBody); // 正则截取标题
                if(name.find()){
                    book.setName(name.group(1));
                }


                MP3File mp3File = (MP3File) AudioFileIO.read(bookAudio.get(0));
                MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
                int audioLength = audioHeader.getTrackLength();
                if(audioLength < 20){
                    waring.add(String.format("%s - %s",audioLength,book.getName()));
                    continue;
                }
                String key = this.upload(bookAudio.get(0));
                //System.out.println("key: " +key);
                HttpClient httpClient = new HttpClient();
                ObjectMapper objectMapper = new ObjectMapper();
                Map response = objectMapper.readValue(httpClient.doPost("http://127.0.0.1:83/book/editAudio","a6625d157635471d86c0a6c4733565c6",new HashMap(){{
                    this.put("name",book.getName());
                    this.put("key",key);
                }}),HashMap.class);
                if(!(response.get("event").toString().equals("SUCCESS"))){
                    waring.add(String.format("%s - %s",-1,book.getName()));
                    continue;
                }
            }catch (Exception e){
                continue;
            }
        }
        for(String str : waring){
            System.out.println(str);
        }
    }

    @Test
    public void qiniu(){
        System.out.println(this.getToken());
    }

    private Auth auth;

    public String upload(File file) throws QiniuException {
        System.out.println("上传了");
        String uploadToken = "jJEBx6Xxlz-7vmKhIAcx73Pbkxv5kcdEP4u3lSS1:9hzGsgo8Z5dsV46J7P-yXmhqygo=:eyJmbmFtZSI6ImhlbGxvLmFwayIsInNjb3BlIjoicGljdHVyZWJvb2siLCJkZWFkbGluZSI6MTYyMDQ4OTU3NX0=";
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        Response response = uploadManager.put(file.getPath(), null, uploadToken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }

    /**
     * 构建七牛Auth对象
     * @return
     */
    public Auth getAuth(){
        if(this.auth == null){
            this.auth = Auth.create("jJEBx6Xxlz-7vmKhIAcx73Pbkxv5kcdEP4u3lSS1","Psz0_EIszVhu1gXGvGHAmdzuo-1PDQP3AV1Bk8PJ");
        }
        return this.auth;
    }

    /**
     * 获取上传token
     * @return
     */
    public String getToken() {
        //jJEBx6Xxlz-7vmKhIAcx73Pbkxv5kcdEP4u3lSS1:ZSt9uPnFDe0o6mVNZVftVFai_hY=:eyJzY29wZSI6InBpY3R1cmVib29rIn0=
        String token = this.getAuth().uploadTokenWithPolicy(this.getDefaultPolicy());
        return token;
    }

    private Map getDefaultPolicy() {
        Map<String,Object> policy = new HashMap();
        policy.put("scope", "picturebook");
        policy.put("deadline", 999999999);
        return policy;
    }
}
