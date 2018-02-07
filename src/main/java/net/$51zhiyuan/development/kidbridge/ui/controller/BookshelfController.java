package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.service.BookshelfService;
import net.$51zhiyuan.development.kidbridge.ui.model.Bookshelf;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 书单
 */
@Controller
@RequestMapping("/bookshelf")
public class BookshelfController {

    @Autowired
    private BookshelfService bookshelfService;

    /**
     * 书单详情
     * @param param id：书单编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/get")
    Message get(@RequestBody Map param){
        // 书单详情
        Bookshelf bookshelf = this.bookshelfService.get(new Bookshelf(){
            @Override
            public Integer getId() {
                return (int)param.get("id");
            }
        });
        return new Message(bookshelf);
    }

    /**
     * 热门书单列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/hot/list")
    Message hot_list(@RequestBody Map param){
        // 热门书单列表
        List<Bookshelf> bookshelfList = this.bookshelfService.hot(new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(bookshelfList);
    }

    /**
     * 今日打卡书单列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/recommend/list")
    Message recommend_list(@RequestBody Map param){
        // 今日打卡书单列表
        List<Bookshelf> bookshelfList = this.bookshelfService.recommend(new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(bookshelfList);
    }

}
