//package net.$51zhiyuan.development.kidbridge.ui.controller;
//
//import net.$51zhiyuan.development.kidbridge.module.Configuration;
//import net.$51zhiyuan.development.kidbridge.service.TokenService;
//import net.$51zhiyuan.development.kidbridge.ui.model.Message;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created by hkhl.cn on 2017/12/16.
// */
//
//@Controller
//@RequestMapping("/token")
//public class TokenController {
//
//    @Autowired
//    private TokenService tokenService;
//
//    @ResponseBody
//    @RequestMapping("/delSession")
//    Object delSession(@RequestBody Map<String,Object> param){
//        String token = (String) param.get("token");
//        String realToken = DigestUtils.sha1Hex(String.format("%s:%s",new SimpleDateFormat("yyyy-MM-dd").format(new Date()), Configuration.SYSTEM_SIGN_SALT));
//        if(!StringUtils.isBlank(token) && token.equals(realToken)){
//            this.tokenService.delSession((int)param.get("id"));
//        }
//        return new Message();
//    }
//}
