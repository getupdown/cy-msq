package cn.cy.core.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import cn.cy.core.msg.QueuedMessage;

/**
 * test
 */
public class TransientMessageQueueTest {

    private volatile TransientMessageQueue messageQueue = MessageQueueFactory.buildTransientQueue();

    private volatile CountDownLatch countDownLatch;

    private void offerData(final int numThread, final int dataPerThread) {

        countDownLatch = new CountDownLatch(numThread);

        for (int i = 0; i < numThread; i++) {
            int tn = i;
            new Thread(() -> {

                for (int j = 0; j < dataPerThread; j++) {
                    QueuedMessage queuedMessage = new QueuedMessage();

                    queuedMessage.setContent("_" + tn + "_" + j);

                    try {
                        messageQueue.push(queuedMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                countDownLatch.countDown();
            }).start();
        }
    }

    /**
     * 检查塞入队列的数据
     *
     * @param dataChecker 消费数据的后置断言
     */
    private void consumeData(Function<List<QueuedMessage>, Boolean> dataChecker, int tn, int tc) throws InterruptedException {

        List<QueuedMessage> consumedMsg = new ArrayList<>();

        CountDownLatch readCountDown = new CountDownLatch(tn * tc);

        new Thread(() -> {

            int x = 0;
            while (true) {
                try {
                    QueuedMessage msg = messageQueue.consumeMsgAt(x);
                    consumedMsg.add(msg);
                    x++;
                    countDownLatch.countDown();
                } catch (QueueAccessOutOfIndexException e) {
                    // ignore
                }
            }

        }).start();

        countDownLatch.await();
        readCountDown.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(dataChecker.apply(consumedMsg));
    }

    /**
     * 测试运行
     */
    @Test
    public void runnableTest() throws InterruptedException {

        int tn = 100;
        int tc = 10000;
        offerData(tn, tc);
        consumeData((queuedMessages -> {
            return queuedMessages.size() == tn * tc;
        }), tn, tc);
    }
}