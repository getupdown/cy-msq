package cn.cy.io.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用于功能测试的基础队列
 */
public enum BasicQueue implements Queue<String> {

    BASIC_QUEUE("basicQueue", "basic", new ConcurrentLinkedQueue<>());

    private String id;

    private String type;

    private ConcurrentLinkedQueue<String> queue;

    BasicQueue(String id, String type, ConcurrentLinkedQueue<String> queue) {
        this.id = id;
        this.type = type;
        this.queue = queue;
    }

    @Override
    public boolean push(String msg) {
        return queue.add(msg);
    }

    @Override
    public String pull() {
        return queue.poll();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
