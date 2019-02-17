package cn.cy.io.vo;

public enum RequestType {

    /**
     * 来自于producer的消息提交
     */
    MESSAGE_COMMIT(0, "消息提交"),

    /**
     * 来自于consumer的消息拉取
     */
    MESSAGE_PULL(1, "消息拖取");

    final public int id;
    final public String desc;

    RequestType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
