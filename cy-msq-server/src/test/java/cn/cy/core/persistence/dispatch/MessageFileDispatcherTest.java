package cn.cy.core.persistence.dispatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import cn.cy.core.persistence.file.AppendInfoWithId;
import cn.cy.core.persistence.file.QueueMsgFile;
import cn.cy.core.queue.QueueConfiguration;

public class MessageFileDispatcherTest {

    private MessageFileDispatcherDebug messageFileDispatcherDebug =
            new MessageFileDispatcherDebug(new QueueConfiguration());

    static class MockMsgFile implements QueueMsgFile {

        Integer id;

        List<String> infoList = new ArrayList<>();

        private final Object sync = new Object();

        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        public MockMsgFile(Integer id) {
            this.id = id;
        }

        @Override
        public String randomRead(Long offset, Long length) throws IOException {
            return null;
        }

        @Override
        public Long getContentCnt() {
            return (long) infoList.size();
        }

        @Override
        public AppendInfoWithId append(CharSequence seq) throws IOException {
            synchronized(sync) {
                infoList.add((String) seq);
            }
            return null;
        }

        @Override
        public Integer getId() {
            return id;
        }

        public void mockAdd() {
            atomicInteger.addAndGet(1);
        }
    }

    private static class Worker implements Runnable {

        private MessageFileDispatcherDebug messageFileDispatcherDebug;

        private CountDownLatch countDownLatch;

        private Integer cnt;

        public Worker(MessageFileDispatcherDebug messageFileDispatcherDebug, CountDownLatch countDownLatch,
                      Integer cnt) {
            this.messageFileDispatcherDebug = messageFileDispatcherDebug;
            this.countDownLatch = countDownLatch;
            this.cnt = cnt;
        }

        @Override
        public void run() {
            for (int i = 0; i < cnt; i++) {
                MockMsgFile msg = (MockMsgFile) messageFileDispatcherDebug.dispatchWrite();
                try {
                    msg.append(Thread.currentThread().getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msg.mockAdd();
            }
            countDownLatch.countDown();
        }
    }

    private void assertions() {

    }

    @Test
    public void test() throws InterruptedException {
        int workerCnt = 20;
        int per = 400000;
        CountDownLatch countDownLatch = new CountDownLatch(workerCnt);
        for (int i = 0; i < workerCnt; i++) {
            Thread thread = new Thread(new Worker(messageFileDispatcherDebug, countDownLatch, per));
            thread.setName("worker_" + i);
            thread.start();
        }
        countDownLatch.await();

        List<QueueMsgFile> queueMsgFiles = messageFileDispatcherDebug.getMsgFiles();

        Map<String, Integer> countMap = new HashMap<>();

        // 1. 被创建的文件id必须连续且id各自唯一
        // 2. 每个content都被塞入了文件, 缺一不可
        for (int i = 0; i < queueMsgFiles.size(); i++) {

            MockMsgFile mockMsgFile = (MockMsgFile) queueMsgFiles.get(i);

            Assert.assertEquals(Integer.valueOf(i), mockMsgFile.getId());

            System.out.println(mockMsgFile.getContentCnt());

            for (int j = 0; j < mockMsgFile.infoList.size(); j++) {
                countMap.compute(mockMsgFile.infoList.get(j), (key, oldValue) -> {
                    if (oldValue == null) {
                        return 1;
                    } else {
                        return oldValue + 1;
                    }
                });
            }
        }
        Assert.assertEquals(countMap.size(), workerCnt);
        countMap.forEach((key, value) -> {
            Assert.assertEquals(value, Integer.valueOf(per));
        });

    }
}