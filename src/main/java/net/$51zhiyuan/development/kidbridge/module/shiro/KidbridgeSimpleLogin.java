package net.$51zhiyuan.development.kidbridge.module.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 一般用户名密码登录
 */
public class KidbridgeSimpleLogin extends UsernamePasswordToken {

    public KidbridgeSimpleLogin() {
    }

    public KidbridgeSimpleLogin(String username, String password) {
        super(username, password);
    }

    public KidbridgeSimpleLogin(String username, String password, String host) {
        super(username, password, host);
    }

}
