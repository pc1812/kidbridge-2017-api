package net.$51zhiyuan.development.kidbridge.exception;

/**
 * 系统级异常，记录系统异常信息
 */
public class KidbridgeSystemException extends RuntimeException {

    public KidbridgeSystemException() {
    }

    public KidbridgeSystemException(String message) {
        super(message);
    }

    public KidbridgeSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public KidbridgeSystemException(Throwable cause) {
        super(cause);
    }

    public KidbridgeSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
