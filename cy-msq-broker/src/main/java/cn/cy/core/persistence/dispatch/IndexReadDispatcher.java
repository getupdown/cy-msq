package cn.cy.core.persistence.dispatch;

import cn.cy.core.persistence.exception.FileNotFoundByIndexException;
import cn.cy.core.persistence.file.RandomAccessible;
import cn.cy.core.queue.index.OffsetIndex;

/**
 * 支持索引的读
 */
public interface IndexReadDispatcher {

    RandomAccessible index(OffsetIndex offsetIndex) throws FileNotFoundByIndexException;

}
