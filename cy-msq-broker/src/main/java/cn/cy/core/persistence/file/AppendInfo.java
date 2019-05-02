package cn.cy.core.persistence.file;

/**
 * 插入信息后的返回结果
 * 用于加入索引
 */
public class AppendInfo {
    private final Long offset;
    private final Long length;

    public AppendInfo(Long offset, Long length) {
        this.offset = offset;
        this.length = length;
    }

    public Long getOffset() {
        return offset;
    }

    public Long getLength() {
        return length;
    }

}
