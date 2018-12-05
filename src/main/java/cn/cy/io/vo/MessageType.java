package cn.cy.io.vo;

public enum MessageType {

    /**
     * 来自于producer的消息提交
     */
    MESSAGE_COMMIT(0, "消息提交"),

    /**
     * 来自于consumer的消息拉取
     */
    MESSAGE_PULL(1, "消息拖取");

    public int id;
    public String desc;

    MessageType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
