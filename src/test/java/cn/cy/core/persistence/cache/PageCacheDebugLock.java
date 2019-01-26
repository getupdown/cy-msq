package cn.cy.core.persistence.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 附加一些debug信息
 */
public class PageCacheDebugLock extends PageCacheLock {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * maxRound, 最多执行maxRound次rebuild
     */
    public Integer maxRound = 0;

    /**
     * key 是 round
     * value 是 每一轮下, 返回的res集合
     * 数组下标是key
     */
    private Map<Integer, ConcurrentLinkedQueue<Lock>[]> testRes = new ConcurrentHashMap<>();

    @Override
    protected void rebuild() throws InterruptedException {
        while (true) {

            if (round > maxRound) {
                break;
            }
            Thread.sleep(rebuildInterval * 1000);

            rebuild0();
        }
    }

    private void initTestRes(ConcurrentLinkedQueue<Lock>[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = new ConcurrentLinkedQueue<>();
        }
    }

    public ConcurrentLinkedQueue<Lock>[] getSpecficRoundRes(int round) {
        return testRes.get(round);
    }

    public PageCacheDebugLock(Integer rebuildInterval, Integer maxRound) {
        super(rebuildInterval);
        this.maxRound = maxRound;

        for (int i = 0; i <= maxRound; i++) {
            ConcurrentLinkedQueue<Lock>[] concurrentLinkedQueues = new ConcurrentLinkedQueue[16];
            initTestRes(concurrentLinkedQueues);
            testRes.put(i, concurrentLinkedQueues);
        }
    }

    @Override
    public Lock compute(Integer key, Callable<Lock> buildFunction) throws ExecutionException, InterruptedException {

        // cas
        while (flag.updateAndGet(prev -> prev >= 0 ? prev + 1 : prev) < 0) {
        }

        Lock res = super.compute(key, buildFunction);

        ConcurrentLinkedQueue<Lock>[] tmp = testRes.get(round);

        if (tmp != null) {
            tmp[key].add(res);
        }

        flag.decrementAndGet();

        return res;
    }
}
