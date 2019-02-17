package cn.cy.core.queue;

public class MessageQueueFactory {

    public static TransientMessageQueue buildTransientQueue() {

        TransientMessageQueue transientMessageQueue = new TransientMessageQueue();

        return transientMessageQueue;
    }
}
