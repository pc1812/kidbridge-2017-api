package net.$51zhiyuan.development.kidbridge.module.shiro;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import org.apache.shiro.session.mgt.SimpleSession;

import java.util.UUID;

/**
 * session id 生成方式实现
 */
public class KidbridgeSession extends SimpleSession {

    public KidbridgeSession() {
        this.setId(UUID.randomUUID().toString().replace("-",""));
        this.setTimeout(Long.parseLong(Configuration.property(Configuration.SESSION_TIMEOUT)));
    }

    public KidbridgeSession(String host) {
        super(host);
        this.setId(UUID.randomUUID().toString().replace("-",""));
        this.setTimeout(Long.parseLong(Configuration.property(Configuration.SESSION_TIMEOUT)));
    }
}
