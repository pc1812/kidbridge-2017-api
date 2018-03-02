package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.service.IOSIAPProductService;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAPProduct;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/iap")
public class IAPController {

    @Autowired
    private IOSIAPProductService iosiapProductService;

    @ResponseBody
    @RequestMapping(value = "/ios")
    Object ios(){
        List<IOSIAPProduct> iapList = this.iosiapProductService.list();
        return new Message(iapList);
    }

    @ResponseBody
    @RequestMapping(value = "/ios/validate")
    Object validate(@RequestBody Map param) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        this.iosiapProductService.validate((int)SecurityUtils.getSubject().getPrincipal(), (String) param.get("receipt-data"));
        return new Message();
    }
}
