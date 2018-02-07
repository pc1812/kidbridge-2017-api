package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.service.FileService;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 七牛
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 获取七牛云资源上传token
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/upload/token")
    Message upload_token(@RequestBody Map param) throws Exception {
        Map<String,Object> data = new HashMap<>();
        String token = this.fileService.getUploadToken((Integer) SecurityUtils.getSubject().getPrincipal(),(Integer) param.get("type"),param.get("option"));
        data.put("token",token);
        return new Message(data);
    }

    /**
     * 七牛云资源上传成功通知，废弃接口
     * @param header
     * @param param
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/upload/callback", produces = "application/json")
    Object upload_callback(@RequestHeader Map header,@RequestBody Map param){
        this.fileService.validCallback((String) header.get("authorization"),"application/json");
        Map m = new HashMap();
        m.put("hello","world");
        return m;
    }

}
