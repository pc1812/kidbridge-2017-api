package net.$51zhiyuan.development.kidbridge.task;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.$51zhiyuan.development.kidbridge.service.CourseService;
import net.$51zhiyuan.development.kidbridge.service.PushService;
import net.$51zhiyuan.development.kidbridge.service.UserBookService;
import net.$51zhiyuan.development.kidbridge.service.UserCourseService;
import net.$51zhiyuan.development.kidbridge.ui.model.SystemPush;
import net.$51zhiyuan.development.kidbridge.ui.model.UserCourse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class DefaultScheduler {

    private final Logger logger = LogManager.getLogger(DefaultScheduler.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private PushService pushService;

    /**
     * 定时器，每天凌晨00:01执行一次，给与用户当天所预习的绘本
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    @Scheduled(cron="*/5 * * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void task() throws APIConnectionException, APIRequestException, JsonProcessingException {
        // 获取当天给与的课程和绘本信息
        List<Map> previewList = this.courseService.getPreviewList();
        for(Map preview : previewList){
            int courseId = (int) preview.get("courseId");
            int bookId = (int) preview.get("bookId");
            String courseName = (String) preview.get("courseName");
            String bookName = (String) preview.get("bookName");
            // 某一课程被哪些用户报名
            List<UserCourse> userCourseList = this.userCourseService.get(courseId);
            for(UserCourse userCourse:userCourseList){
                // 给与绘本
                int resultRow = this.userBookService.add(userCourse.getUser().getId(),bookId,true,new HashMap(){{
                    this.put("type",0);
                    this.put("course",courseId);
                    this.put("timestamp",new Date());
                }});
                // 消息通知
                if(resultRow > 0){
                    SystemPush systemPush = new SystemPush();
                    systemPush.setMessage(String.format("您的课程《%s》中的绘本《%s》已开放预习，快快去跟读吧 ~",courseName,bookName));
                    this.pushService.sendSystemPush(userCourse.getUser().getId(),systemPush);
                }
            }
        }
    }
}
