package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统消息推送
 */
public class SystemPush implements Serializable {

    private Map message;

    public Map getMessage() {
        return message;
    }

    public void setMessage(String text) {
        this.message = new HashMap();
        this.message.put("text",text);
        this.message.put("createTime",new Date());
    }
}
