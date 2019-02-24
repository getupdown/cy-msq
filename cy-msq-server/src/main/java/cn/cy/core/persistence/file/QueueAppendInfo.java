package cn.cy.core.persistence.file;

public class QueueAppendInfo {

    private final AppendInfo appendInfo;

    private final Integer id;

    public QueueAppendInfo(AppendInfo appendInfo, Integer id) {
        this.appendInfo = appendInfo;
        this.id = id;
    }

    public AppendInfo getAppendInfo() {
        return appendInfo;
    }

    public Integer getId() {
        return id;
    }
}
