package cn.cy.core.persistence.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cy.common.ConcurrentFinalCache;

/**
 * page cache lock
 * 使用暴力重构策略
 * 每过{@link PageCacheLock#rebuildInterval}时间, 进行一次暴力重构
 * <p>
 * 使用 {@link PageCacheLock#flag} 来模拟读写锁的功能
 */
public class PageCacheLock extends ConcurrentFinalCache<Integer, Lock> {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * flag == {@link PageCacheLock#REBUILDING} , 意味着重构线程正在执行
     * flag > 0 , 意味着有线程在读取, 不可重构
     */
    protected AtomicInteger flag = new AtomicInteger(0);

    protected final Integer INIT = 0;

    protected final Integer REBUILDING = -1;

    /**
     * 重构时间间隔, 默认300s
     */
    protected Integer rebuildInterval;

    /**
     * 每一次rebuild 轮数+1
     */
    public Integer round = 0;

    public PageCacheLock() {
        this(300);
    }

    public PageCacheLock(Integer rebuildInterval) {
        this.rebuildInterval = rebuildInterval;
    }

    public void initialize() {

        new Thread(() -> {
            try {
                rebuild();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 重构
     *
     * @throws InterruptedException
     */
    protected void rebuild() throws InterruptedException {

        while (true) {

            Thread.sleep(rebuildInterval * 1000);

            rebuild0();
        }
    }

    protected void rebuild0() {
        LOGGER.debug("rebuild awaiting ... ");

        // cas
        // 由于netty线程模型问题
        // 所以不会出现海量线程争抢
        // rebuild任务饥饿的概率较小
        while (!flag.compareAndSet(INIT, REBUILDING)) {
        }

        LOGGER.debug("set into rebuild status ... ");

        futureMap = new ConcurrentHashMap<>();

        round++;

        // 这时一定只有这个线程在运行, 所以直接设置回去就行
        flag.set(INIT);
    }

    /**
     * @param key           key
     * @param buildFunction the function to generate the value
     *
     * @return
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Lock compute(Integer key, Callable<Lock> buildFunction) throws ExecutionException, InterruptedException {

        // cas
        while (flag.updateAndGet(prev -> prev >= 0 ? prev + 1 : prev) < 0) {
        }

        Lock res = super.compute(key, buildFunction);

        // 这个锁的临界区必须置于此
        // 否则中间被rebuild打断, 可能造成前后拿的锁不一致
        // todo 还没写完 这里还要改

        flag.decrementAndGet();

        return res;
    }
}
