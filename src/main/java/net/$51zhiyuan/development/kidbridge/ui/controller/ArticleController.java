package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.service.ArticleService;
import net.$51zhiyuan.development.kidbridge.ui.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 文章
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章详情
     * @param id 文章编号
     * @return
     */
    @RequestMapping("/{id}")
    ModelAndView detail(@PathVariable String id){
        ModelAndView view = new ModelAndView("/article");
        // 文章详情
        Article article = this.articleService.get(new Article(){
            @Override
            public Integer getId() {
                return Integer.valueOf(id);
            }
        });
        // 七牛云资源域名
        view.addObject("bucketDomain", Configuration.property(Configuration.QINIU_BUCKET_DOMAIN));
        // 文章信息
        view.addObject("article",article);
        return view;
    }
}
