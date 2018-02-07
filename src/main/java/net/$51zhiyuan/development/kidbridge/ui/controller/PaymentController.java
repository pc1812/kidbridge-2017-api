package net.$51zhiyuan.development.kidbridge.ui.controller;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.service.PaymentService;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 微信支付
     * @param param fee：支付金额
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/wechat")
    Message wechat(@RequestBody Map<String,Object> param) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, SAXException, ParserConfigurationException, TransformerException, ParseException {
        Subject subject = SecurityUtils.getSubject();
        // 支付参数
        Map paymentParam = this.paymentService.wechatPayment((int)subject.getPrincipal(),new BigDecimal(String.valueOf(Double.parseDouble(param.get("fee").toString()))),subject.getSession().getHost());
        return new Message(new HashMap(){{
            this.put("payment",paymentParam);
        }});
    }

    /**
     * 微信支付成功通知
     * @param param
     * @return
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    @ResponseBody
    @RequestMapping(value = "/wechat/callback")
    Object wechat_callback(@RequestBody String param) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.paymentService.wechatCallback(param);
    }

    /**
     * 支付宝支付
     * @param param fee：支付金额
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws ParseException
     * @throws AlipayApiException
     */
    @ResponseBody
    @RequestMapping(value = "/alipay")
    Message alipay(@RequestBody Map<String,Object> param) throws IOException, ParseException, AlipayApiException {
        Subject subject = SecurityUtils.getSubject();
        // 支付参数
        String paymentParam = this.paymentService.alipayPayment((int)subject.getPrincipal(),new BigDecimal(String.valueOf(Double.parseDouble(param.get("fee").toString()))));
        return new Message(new HashMap(){{
            this.put("payment",paymentParam);
        }});
    }

    /**
     * 支付宝支付成功通知
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping(value = "/alipay/callback")
    Object alipay_callback(@RequestParam Map param) throws UnsupportedEncodingException, AlipayApiException {
        return this.paymentService.alipayCallback(param);
    }

}
