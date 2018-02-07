package net.$51zhiyuan.development.kidbridge.module.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * shiro安全管理实现
 */
public class KidbridgeSecurityManager extends DefaultWebSecurityManager {

    /**
     * 用户退出登录
     * @param subject
     */
    @Override
    protected void stopSession(Subject subject) {
        Session session = subject.getSession();
        super.stopSession(subject);
        KidbridgeSessionManager sessionManager = (KidbridgeSessionManager) super.getSessionManager();
        // 删除redis中存储的会话信息
        sessionManager.delete(session);
    }
}
