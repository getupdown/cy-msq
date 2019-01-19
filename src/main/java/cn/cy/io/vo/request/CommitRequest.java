package cn.cy.io.vo.request;

/**
 * producer生产消息
 */
public class CommitRequest {

    /**
     * 消息本体
     */
    private String msg;

    //要发送到的队列id
    private String distQueue;

    //是否需要返回确认消息
    private boolean needAck;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isNeedAck() {
        return needAck;
    }

    public void setNeedAck(boolean needAck) {
        this.needAck = needAck;
    }
}
