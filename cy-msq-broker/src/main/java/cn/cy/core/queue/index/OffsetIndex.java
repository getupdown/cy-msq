package cn.cy.core.queue.index;

public class OffsetIndex {

    private String checkSum;
    private Long byteOffset;
    private Long length;
    private Long msgOffset;
    private Integer fileId;

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public Long getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(Long byteOffset) {
        this.byteOffset = byteOffset;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getMsgOffset() {
        return msgOffset;
    }

    public void setMsgOffset(Long msgOffset) {
        this.msgOffset = msgOffset;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
}
