package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;

/**
 * 消息类别
 */
public enum MessageType implements Serializable {
    // 响应成功
    SUCCESS,
    // 需要跳转界面
    REDIRECT,
    // 未登录
    UNAUTH,
    // 无相应权限
    UNPERM,
    // 无相应角色
    UNROLE,
    // token过期、不存在
    UNTOKEN,
    // 一般性错误
    ERROR,
    // 系统级异常
    EXCEPTION,
    // 未知的版本号
    UNVERSION,
    // 余额不足
    INSUFFICIENT_BALANCE,
    // 水滴不足
    INSUFFICIENT_BONUS,
    // 用户不存在
    UNUSER,
    // 登录异常，多设备登录
    AUTH_ABNORMAL,
    // 用户已禁用
    AUTH_DISABLE
}
