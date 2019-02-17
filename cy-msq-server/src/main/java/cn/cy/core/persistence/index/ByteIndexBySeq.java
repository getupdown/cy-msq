package cn.cy.core.persistence.index;

/**
 * seq -> (offset, length)
 */
public interface ByteIndexBySeq {

    Long getOffsetBySeq(Long seq);

    Long getLengthBySeq(Long seq);

}
