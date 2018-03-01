package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.service.IOSIAPService;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAP;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/iap")
public class IAPController {

    @Autowired
    private IOSIAPService iosiapService;

    @ResponseBody
    @RequestMapping(value = "/ios")
    Object ios(){
        List<IOSIAP> iapList = this.iosiapService.list();
        return new Message(iapList);
    }
}
