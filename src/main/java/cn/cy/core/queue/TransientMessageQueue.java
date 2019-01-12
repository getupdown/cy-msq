package cn.cy.core.queue;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.cy.core.msg.QueuedMessage;

/**
 * 基于内存的瞬时态队列
 * <p>
 * 使用类似于读写分离的思想
 * 首先在一个后备队列上写，这个后备队列可以使用 {@link java.util.concurrent.ConcurrentLinkedQueue} 实现
 * 因为netty的线程模型, 所以这个必然不会出现大量线程竞争
 * <p>
 * 然后定时轮询, 同步到读集合里, 消费者都从读集合里读取消息
 */
public class TransientMessageQueue implements MessageQueue {

    // 可读队列开头所表示的偏移量
    private int startOffsetOfReadQueue;

    // 可随机读队列
    private ArrayList<QueuedMessage> readRandomQueue = new ArrayList<>();

    // 被写队列
    private ConcurrentLinkedQueue<QueuedMessage> writeFIFOQueue = new ConcurrentLinkedQueue<>();

    // 每次轮询最大读取元素个数
    private int POOLING_MAX_CNT = 10000;

    // 用于控制队列状态的读写锁
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // status
    // 0 : read
    // 1 : write
    private int status = 0;

    /**
     * 把消息加入被写队列
     *
     * @param queuedMessage
     *
     * @throws Exception
     */
    @Override
    public void push(QueuedMessage queuedMessage) throws Exception {

        boolean offerRes = writeFIFOQueue.offer(queuedMessage);

        // 加入队列失败
        if (!offerRes) {
            // todo log
        }
    }

    /**
     * 从读区域里消费指定偏移量的消息
     *
     * @param offset
     *
     * @return
     */
    @Override
    public QueuedMessage consumeMsgAt(int offset) {

        Lock lock = readWriteLock.readLock();

        int targetIndex = offset - startOffsetOfReadQueue;
        try {
            lock.lock();
            return readRandomQueue.get(targetIndex);
        } catch (IndexOutOfBoundsException oob) {
            throw new QueueAccessOutOfIndexException(targetIndex);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 定时轮询, 从写队列里拿元素, 放进读队列
     */
    private void polling() {
        Lock lock = readWriteLock.writeLock();

        try {
            lock.lock();

            for (int i = 0; i < POOLING_MAX_CNT; i++) {
                QueuedMessage msg = writeFIFOQueue.poll();
                if (msg == null) {
                    break;
                }
                readRandomQueue.add(msg);
            }

        } finally {
            lock.unlock();
        }
    }
}
