package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.service.BillService;
import net.$51zhiyuan.development.kidbridge.ui.model.Bill;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 费用
 */
@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * 用户费用明细列表
     * @param param feeType 费用类别，0现金余额，1水滴
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    Message list(@RequestBody Map<String,Object> param){
        // 明细列表
        List<Bill> billList = this.billService.list(new Bill(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        // 当前登录用户id
                        return (int) SecurityUtils.getSubject().getPrincipal();
                    }
                };
            }

            @Override
            public Integer getFeeType() {
                // 费用类别
                return (int) param.get("feeType");
            }
        },new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT))); // 分页
        return new Message(billList);
    }

}
