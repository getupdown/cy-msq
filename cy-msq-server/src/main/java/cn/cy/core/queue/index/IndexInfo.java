package cn.cy.core.queue.index;

import java.util.List;

import cn.cy.core.persistence.file.SingleFileByteIndexImpl;

/**
 * indexInfo
 */
public class IndexInfo {

    // index描述
    private String description;

    // 所有索引文件的集合
    private List<SingleFileByteIndexImpl> indexFiles;



}
