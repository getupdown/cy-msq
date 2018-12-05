package cn.cy.io.vo.request;

import cn.cy.io.vo.AbstractMessage;

/**
 * producer生产消息
 */
public class CommitRequest extends AbstractMessage {

    /**
     * 消息本体
     */
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
