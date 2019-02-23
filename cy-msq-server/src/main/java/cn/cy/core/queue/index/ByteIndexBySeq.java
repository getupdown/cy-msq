package cn.cy.core.queue.index;

/**
 * seq -> (offset, length)
 */
public interface ByteIndexBySeq {

    OffsetIndex getIndexBySeq(Long msgOffset);

    void insertIndex(Long msgOffset, OffsetIndex offsetIndex);
}
