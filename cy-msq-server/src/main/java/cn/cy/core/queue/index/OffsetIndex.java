package cn.cy.core.queue.index;

public class OffsetIndex {

    private String checkSum;
    private Long byteOffset;
    private Long length;
    private Long msgOffset;

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public Long getByteOffset() {
        return byteOffset;
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
}
