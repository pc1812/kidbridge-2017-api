package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.service.FeedbackService;
import net.$51zhiyuan.development.kidbridge.ui.model.Feedback;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 意见反馈
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 用户意见反馈提交
     * @param param content：反馈内容
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    Message add(@RequestBody Map<String,Object> param){
        // 新增反馈
        this.feedbackService.add(new Feedback(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return (int) SecurityUtils.getSubject().getPrincipal();
                    }
                };
            }

            @Override
            public String getContent() {
                return (String) param.get("content");
            }
        });
        return new Message();
    }
}
