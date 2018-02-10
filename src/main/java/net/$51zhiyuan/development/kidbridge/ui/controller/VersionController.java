package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/version")
public class VersionController {

    @ResponseBody
    @RequestMapping(value = "/check")
    Object check(HttpServletRequest request){

        return new Message();
    }
}
