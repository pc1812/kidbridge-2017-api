package net.$51zhiyuan.development.kidbridge.module.shiro;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.Util;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import net.$51zhiyuan.development.kidbridge.service.PushService;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.MessageType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * shiro授权过滤器扩展
 */
public class KidbridgeAuthenticationFilter extends AccessControlFilter {

    private final Logger logger = LogManager.getLogger(KidbridgeAuthenticationFilter.class);

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();

    @Autowired
    private PushService pushService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        String requestURI = super.getPathWithinApplication(request);
        for(String path : Configuration.SYSTEM_SHIRO_LOGIN_URL){
            if(super.pathMatcher.matches(path,requestURI)){
                return true;
            }
        }
        return false;
    }

    /**
     * 请求校验
     * @param servletRequest
     * @param servletResponse
     * @param mappedValue
     * @return
     * @throws IOException
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String sign = request.getHeader(Configuration.SYSTEM_REQUEST_SIGN);
        String token = request.getHeader(Configuration.SYSTEM_REQUEST_TOKEN);
        String timestamp = request.getHeader(Configuration.SYSTEM_REQUEST_TIMESTAMP);
        String version = request.getHeader(Configuration.SYSTEM_REQUEST_VERSION);
        // 必要请求参数检查
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(version)) {
            return this.onAccessDeniedHandle(servletRequest,servletResponse,new Message(MessageType.ERROR, "非法的请求参数"));
        }

        // 参数签名验证
        String validateSign = Util.sign(new HashMap(){{
            this.put("uri",uri);
            this.put("token",token);
            this.put("timestamp",timestamp);
            this.put("version",version);
        }});

        if (!(sign.toLowerCase().equals(validateSign.toLowerCase()))) {
            // 签名不匹配
            return this.onAccessDeniedHandle(servletRequest,servletResponse,new Message(MessageType.ERROR, "非法的参数签名"));
        }

        if(!this.isLoginRequest(servletRequest, servletResponse) && StringUtils.isBlank(token)){
            // 非登录接口的请求，必须携带token
            return this.onAccessDeniedHandle(servletRequest,servletResponse,new Message(MessageType.UNTOKEN,"请求未授权"));
        }
        Subject subject = super.getSubject(servletRequest, servletResponse);

        if(!this.isLoginRequest(servletRequest, servletResponse) && !subject.isAuthenticated()){
            // token已过期
            return this.onAccessDeniedHandle(servletRequest,servletResponse,new Message(MessageType.UNAUTH,"请求未授权"));
        }

        // 通过用户ID取到所对应的token
        String sessionMapping = (String) this.redisTemplate.opsForValue().get(String.format("%s:%s",Configuration.SYSTEM_SESSION_MAPPING_NAMESPACE,subject.getPrincipal()));
        if(!StringUtils.isBlank(sessionMapping)){
            if(!(subject.getSession(false).getId().toString().equals(sessionMapping))){
                // 如果当前用户请求的token与最新存储的token不一致，说明账号已在其它设备重新登录，注销当前用户会话
                // 该功能主要防止同一用户在多设备使用，造成的消息通知丢失
                subject.logout();
                return this.onAccessDeniedHandle(servletRequest,servletResponse,new Message(MessageType.AUTH_ABNORMAL,"会话失效，该账户已在其它终端登录 ~"));
            }
        }

        // 程序到这一步说明当前用户正在活跃，推送历史存储的消息
        onActive(servletRequest, servletResponse);
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) {

        return false;
    }

    /**
     * 请求未通过验证，返回提示消息
     * @param servletRequest
     * @param servletResponse
     * @param message
     * @return
     * @throws IOException
     */
    private boolean onAccessDeniedHandle(ServletRequest servletRequest, ServletResponse servletResponse, Message message) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Util.printJsonMessage(response,message);
        return false;
    }

    private void onActive(ServletRequest servletRequest, ServletResponse servletResponse) {
        Subject subject = super.getSubject(servletRequest, servletResponse);
        if(!this.isLoginRequest(servletRequest, servletResponse)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pushService.sendQueue((int) subject.getPrincipal());
                    } catch (JsonProcessingException e) {
                        logger.error(e.getMessage(),e);
                    }
                }
            }).start();
        }
    }

}
