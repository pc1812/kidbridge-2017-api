package net.$51zhiyuan.development.kidbridge.exception;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.Util;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.MessageType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常统一处理
 */
public class KidbridgeExceptionHandler {

    private final Logger logger = LogManager.getLogger(KidbridgeExceptionHandler.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    public void doAfterThrowing(Exception e) throws IOException {
        Throwable throwable = e;
        while (throwable != null){
            if(throwable instanceof KidbridgeSimpleException){
                break;
            }
            throwable = throwable.getCause();
        }
        if(!(throwable instanceof KidbridgeSimpleException)){
            throwable = e;
        }
        String message = throwable.getMessage();
        // 如果仅为一般的操作性错误，直接返回给客户端，进行toast提示
        if(throwable instanceof KidbridgeSimpleException){
            this.logger.info(message,throwable);
            KidbridgeSimpleException pictureBookSimpleException = (KidbridgeSimpleException) throwable;
            MessageType messageType = pictureBookSimpleException.getMessageType() == null ? MessageType.ERROR : pictureBookSimpleException.getMessageType();
            Object data = pictureBookSimpleException.getData();
            Util.printJsonMessage(this.httpServletResponse,(data == null ? new Message(messageType,message) : new Message(messageType,data,message)));
            return;
        }
        // 否则如果为系统异常，进行日志记录
        this.logger.error(message,throwable);
        String exceptionTip = null;
        try {
            // 默认系统异常下，返回给客户端的toast提示
            // 系统繁忙，请稍后再试 ~
            exceptionTip = Configuration.property(Configuration.EXCEPTION_TIP);
        }finally {
            Util.printJsonMessage(this.httpServletResponse,new Message(MessageType.ERROR,StringUtils.isBlank(exceptionTip) ? "project exception" : exceptionTip));
        }
    }

}
