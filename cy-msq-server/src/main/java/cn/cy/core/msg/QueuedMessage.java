package cn.cy.core.msg;

/**
 * 已入队的消息
 */
public class QueuedMessage {

    // 所属的队列id
    private String qid;

    // 所在偏移量
    private int offset;

    // 内容
    private String content;

    private String checkSum;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
