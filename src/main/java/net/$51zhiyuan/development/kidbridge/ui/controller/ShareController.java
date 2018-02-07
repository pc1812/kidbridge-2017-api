package net.$51zhiyuan.development.kidbridge.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.$51zhiyuan.development.kidbridge.service.UserService;
import net.$51zhiyuan.development.kidbridge.ui.model.UserBookRepeat;
import net.$51zhiyuan.development.kidbridge.ui.model.UserCourseRepeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 分享
 */
@Controller
@RequestMapping("/share")
public class ShareController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    /**
     * 用户绘本跟读分享H5页面
     * @param id 绘本跟读ID
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/book/repeat/{id}")
    Object book_repeat(@PathVariable String id) throws JsonProcessingException {
        ModelAndView view = new ModelAndView("/share/repeat/book");
        UserBookRepeat userBookRepeat = this.userService.getUserBookRepeat(Integer.valueOf(id));
        view.addObject("book",userBookRepeat.getUserBook().getBook()); // 绘本信息
        view.addObject("user",userBookRepeat.getUserBook().getUser()); // 跟读用户信息
        view.addObject("repeat",this.objectMapper.writeValueAsString(userBookRepeat.getSegment())); // 跟读段落信息
        return view;
    }

    /**
     * 用户课程跟读分享界面
     * @param id 课程跟读ID
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/course/repeat/{id}")
    Object course_repeat(@PathVariable String id) throws JsonProcessingException {
        ModelAndView view = new ModelAndView("/share/repeat/course");
        UserCourseRepeat userCourseRepeat = this.userService.getUserCourseRepeat(Integer.valueOf(id));
        view.addObject("course",userCourseRepeat.getUserCourse().getCourse()); // 课程信息
        view.addObject("user",userCourseRepeat.getUserCourse().getUser()); // 跟读用户信息
        view.addObject("repeat",this.objectMapper.writeValueAsString(userCourseRepeat.getSegment())); // 跟读段落信息
        return view;
    }
}
