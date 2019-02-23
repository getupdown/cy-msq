package cn.cy.core.persistence.exception;

/**
 * 索引重复建立的异常
 */
public class DuplicateIndexException extends RuntimeException {

    public DuplicateIndexException() {
    }

    public DuplicateIndexException(String message) {
        super(message);
    }

    public DuplicateIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateIndexException(Throwable cause) {
        super(cause);
    }
}
