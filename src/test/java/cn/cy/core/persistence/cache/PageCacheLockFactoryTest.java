package cn.cy.core.persistence.cache;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PageCacheLockFactoryTest {

    @Before
    public void init() {
    }

    public static class WorkModule implements Runnable {
        private final PageCacheDebugLockFactory pageCacheLock;

        private final Integer reqInterval;

        public WorkModule(PageCacheDebugLockFactory pageCacheLock, Integer reqInterval) {
            this.pageCacheLock = pageCacheLock;
            this.reqInterval = reqInterval;
        }

        @Override
        public void run() {

            while (true) {

                try {
                    Thread.sleep(reqInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Random rd = new Random();
                Integer key = Math.abs(rd.nextInt() % 16);

                try {
                    Lock res = pageCacheLock.compute(key, ReentrantLock::new);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testRunnable() throws InterruptedException {

        PageCacheDebugLockFactory pageCacheLock = new PageCacheDebugLockFactory(20, Integer.MAX_VALUE);

        // 启动8个线程
        // 验证可行性
        int threadCnt = 8;

        for (int i = 0; i < threadCnt; i++) {
            WorkModule workModule = new WorkModule(pageCacheLock, 50);
            new Thread(workModule).start();
        }
        pageCacheLock.initialize();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        countDownLatch.await(20, TimeUnit.SECONDS);
    }

    /**
     * 验证正确性
     * <p>
     * 要求同一个key拿到的锁一定要是同一把
     * 这里的同一把指的是内存上的相同
     * <p>
     * 1. 测试在没有rebuild的情况下, 同一个key不会拿到不同的两个对象
     */
    @Test
    public void testWithoutRebuild() throws InterruptedException {

        // 间隔设置大, 以模拟不会rebuild的情况
        PageCacheDebugLockFactory pageCacheLock = new PageCacheDebugLockFactory(10000, 20);

        pageCacheLock.initialize();

        int threadCnt = 8;

        for (int i = 0; i < threadCnt; i++) {
            WorkModule workModule = new WorkModule(pageCacheLock, 50);
            new Thread(workModule).start();
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);

        countDownLatch.await(20, TimeUnit.SECONDS);

        checkResSet(pageCacheLock.getSpecficRoundRes(0));
    }

    private void checkResSet(ConcurrentLinkedQueue<Lock>[] testRes) {
        // 验证结果
        for (int i = 0; i < testRes.length; i++) {
            Lock tmp = null;
            System.out.println("key is " + i + " length is " + testRes[i].size());
            Lock[] save = testRes[i].toArray(new Lock[0]);
            for (int j = 0; j < testRes[i].size(); j++) {
                Lock cmp = save[j];

                if (tmp != null) {
                    Assert.assertEquals(tmp, cmp);
                }
                tmp = cmp;

            }
        }
    }

    /**
     * 验证正确性2
     * <p>
     * 2. 测试在有rebuild的情况下, rebuild前后, 同一个key不会拿到不同的两个对象
     * 包含了上面的测试
     */
    @Test
    public void testWithRebuild() throws InterruptedException {

        PageCacheDebugLockFactory pageCacheLock = new PageCacheDebugLockFactory(5, 100);

        pageCacheLock.initialize();

        int threadCnt = 8;

        for (int i = 0; i < threadCnt; i++) {
            WorkModule workModule = new WorkModule(pageCacheLock, 50);
            new Thread(workModule).start();
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);

        countDownLatch.await(20, TimeUnit.SECONDS);

        for (int i = 0; i < pageCacheLock.maxRound; i++) {
            checkResSet(pageCacheLock.getSpecficRoundRes(i));
        }

        // 这里检验不同组之间, 会不会使用了同一个lock
        // 表示上一轮中, 每个key分别的lock
        ArrayList<Lock> save = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            save.add(null);
        }

        for (int i = 0; i < pageCacheLock.maxRound; i++) {
            ConcurrentLinkedQueue<Lock>[] locks = pageCacheLock.getSpecficRoundRes(i);
            for (int j = 0; j < 16; j++) {
                if (save.get(j) == null || locks[j] == null) {
                    continue;
                }
                // 由于上面已经测试过, 队列里全是一样的
                // 所以这里直接拿第一个元素即可
                Lock cmp = locks[j].toArray(new Lock[0])[0];

                // 不同组的元素不同才对
                Assert.assertNotEquals(cmp, save.get(j));

                save.set(j, cmp);
            }
        }
    }
}