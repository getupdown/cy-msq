package cn.cy.client.core.producer;

import cn.cy.io.vo.BaseInfo;
import cn.cy.io.vo.RequestType;
import cn.cy.io.vo.request.CommitRequest;
import com.alibaba.fastjson.JSON;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 处理消息发送的可执行任务
 */
public class Sender implements Runnable{

    private volatile boolean isRunning;

    private ConcurrentLinkedQueue<BaseInfo<CommitRequest>> pendingQueue;

    private IProducer producer;

    public Sender() {
    }

    public Sender(IProducer producer) {
        this.producer = producer;
        this.isRunning = true;
        this.pendingQueue = new ConcurrentLinkedQueue<>();
    }

    private BaseInfo<CommitRequest> construct(String message) {
        CommitRequest request = new CommitRequest(message);
        return new BaseInfo<>(RequestType.MESSAGE_COMMIT.id, null, UUID.randomUUID().toString(), request);
    }

    public void send(String message) {
        BaseInfo<CommitRequest> req = construct(message);
        pendingQueue.add(req);
    }

    @Override
    public void run() {
        while(isRunning) {
            pollPending();
        }
    }

    private void pollPending() {
        if (!pendingQueue.isEmpty()) {
            BaseInfo<CommitRequest> req = pendingQueue.poll();
            if (req == null) {
                return;
            }
            producer.getChannel().asyncWrite(JSON.toJSONString(req));
        }
    }
}
