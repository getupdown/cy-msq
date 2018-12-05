package cn.cy.io.vo;

/**
 * 所有消息的基类
 */
public abstract class AbstractMessage {

    private Integer type;

    private String desc;

    private String requestId;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
