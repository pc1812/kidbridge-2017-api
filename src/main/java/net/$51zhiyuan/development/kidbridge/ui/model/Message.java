package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 返回消息模型
 */
public class Message implements Serializable {


    private String event = MessageType.SUCCESS.name();
    private Object data = new ArrayList<>();
    private String describe = "";

    public Message(){

    }

    public Message(Object data) {
        this.setData(data);
    }

    public Message(String describe) {
        this.setDescribe(describe);
    }

    public Message(Object data, String describe) {
        this.setData(data);
        this.setDescribe(describe);
    }

    public Message(MessageType event, String describe) {
        this.event = event.name();
        this.setDescribe(describe);
    }

    public Message(MessageType event, Object data) {
        this.event = event.name();
        this.setData(data);
    }

    public Message(MessageType event, Object data, String describe) {
        this.event = event.name();
        this.setData(data);
        this.setDescribe(describe);
    }

    public String getEvent() {
        return event;
    }

    public Message setEvent(MessageType event) {
        this.event = event.name();
        return this;
    }

    public Object getData() {
        return data;
    }

    public Message setData(Object data) {
        if(data != null){
            this.data = data;
        }
        return this;
    }

    public String getDescribe() {
        return describe;
    }

    public Message setDescribe(String describe) {
        if(describe != null){
            this.describe = describe;
        }
        return this;
    }

}
