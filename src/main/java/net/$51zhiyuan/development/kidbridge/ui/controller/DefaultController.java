package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

    /**
     * 首页缺省
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/")
    Object index(){
        return new Message("1.0.0");
    }
}
