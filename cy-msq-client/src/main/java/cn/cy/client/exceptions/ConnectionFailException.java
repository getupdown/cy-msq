package cn.cy.client.exceptions;

public class ConnectionFailException extends RuntimeException{

    public ConnectionFailException() {
    }

    public ConnectionFailException(String message) {
        super(message);
    }

    public ConnectionFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionFailException(Throwable cause) {
        super(cause);
    }

    public ConnectionFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
