package cn.cy.core.queue;

/**
 * 队列访问越界异常
 */
public class QueueAccessOutOfIndexException extends IndexOutOfBoundsException {

    public QueueAccessOutOfIndexException(int index, MessageQueue queue) {
        super("out of bound ! index is : " + index + " queue is : " + queue.toString());
    }

    public QueueAccessOutOfIndexException(int index) {
        super("out of bound ! index is : " + index);
    }
}
