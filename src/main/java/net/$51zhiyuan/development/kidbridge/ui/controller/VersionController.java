package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.service.VersionService;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/version")
public class VersionController {

    @Autowired
    private VersionService versionService;

    @ResponseBody
    @RequestMapping(value = "/check")
    Object check(HttpServletRequest request){
        String device = request.getHeader(Configuration.SYSTEM_REQUEST_DEVICE);
        Version version = this.versionService.check(device);
        return new Message(new HashMap(){{
            this.put("content",version.getContent());
            this.put("number",version.getNumber());
        }});
    }
}
