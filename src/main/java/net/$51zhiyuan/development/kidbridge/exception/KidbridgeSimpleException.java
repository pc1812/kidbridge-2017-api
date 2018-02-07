package net.$51zhiyuan.development.kidbridge.exception;

import net.$51zhiyuan.development.kidbridge.ui.model.MessageType;

import java.util.ArrayList;

/**
 * 操作性异常，异常信息可直接返回给客户端，进行toast提示
 */
public class KidbridgeSimpleException extends KidbridgeSystemException {

    private Object data;

    private MessageType messageType;

    public KidbridgeSimpleException() {

    }

    public KidbridgeSimpleException(String message) {
        super(message);
    }

    public KidbridgeSimpleException(MessageType messageType, String message) {
        super(message);
        this.messageType = messageType;
        this.data = new ArrayList<>();
    }

    public KidbridgeSimpleException(MessageType messageType , Object data, String message) {
        super(message);
        this.messageType = messageType;
        this.data = data;
    }

    public KidbridgeSimpleException(String message, Throwable cause) {
        super(message, cause);
    }

    public KidbridgeSimpleException(Throwable cause) {
        super(cause);
    }

    public KidbridgeSimpleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
