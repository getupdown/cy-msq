package cn.cy.io.vo;

/**
 * 所有消息(请求, 响应)的基类
 */
public class BaseInfo<T> {

    private Integer type;

    private String desc;

    private String requestId;

    private T data;

    public BaseInfo() {
    }

    public BaseInfo(Integer type, String desc, String requestId, T data) {
        this.type = type;
        this.desc = desc;
        this.requestId = requestId;
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
