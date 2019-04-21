package cn.cy.core.queue.index;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用于测试, 基于内存的索引
 */
public class MemoryByteIndexImpl implements ByteIndexBySeq {

    private ConcurrentMap<Long, OffsetIndex> concurrentMap = new ConcurrentHashMap<>();

    @Override
    public OffsetIndex getIndexBySeq(Long msgOffset) {
        return concurrentMap.getOrDefault(msgOffset, null);
    }

    @Override
    public void insertIndex(Long msgOffset, OffsetIndex offsetIndex) {
        concurrentMap.put(msgOffset, offsetIndex);
    }
}
