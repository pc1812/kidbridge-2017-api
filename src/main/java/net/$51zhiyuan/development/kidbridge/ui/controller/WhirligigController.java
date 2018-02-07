package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.service.WhirligigService;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.Whirligig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 轮播图
 */
@Controller
@RequestMapping("/whirligig")
public class WhirligigController {

    @Autowired
    private WhirligigService whirligigService;

    /**
     * 获取轮播图列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    Message list(@RequestBody Map<String,Object> param){
        List<Whirligig> whirligigList = this.whirligigService.list(new Whirligig(),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(whirligigList);
    }




}
