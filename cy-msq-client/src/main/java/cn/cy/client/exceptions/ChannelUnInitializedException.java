package cn.cy.client.exceptions;

public class ChannelUnInitializedException extends RuntimeException{

    public ChannelUnInitializedException() {
    }

    public ChannelUnInitializedException(String message) {
        super(message);
    }

    public ChannelUnInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelUnInitializedException(Throwable cause) {
        super(cause);
    }

    public ChannelUnInitializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
