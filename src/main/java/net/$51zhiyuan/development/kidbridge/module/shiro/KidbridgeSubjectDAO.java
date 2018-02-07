package net.$51zhiyuan.development.kidbridge.module.shiro;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class KidbridgeSubjectDAO extends DefaultSubjectDAO {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    protected void saveToSession(Subject subject) {
        Session session = subject.getSession(false);
        if (session == null && !CollectionUtils.isEmpty(subject.getPrincipals())) {
            session = subject.getSession();
            // 用户登录成功后，保存用户ID与session id关联信息
            this.redisTemplate.opsForValue().set(String.format("%s:%s",Configuration.SYSTEM_SESSION_MAPPING_NAMESPACE,subject.getPrincipal()),session.getId(),session.getTimeout(), TimeUnit.MILLISECONDS);
        }
        super.saveToSession(subject);
    }


}
