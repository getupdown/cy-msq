package cn.cy.core.persistence.file;

public class AppendInfoWithId {

    private final AppendInfo appendInfo;

    private final Integer id;

    public AppendInfoWithId(AppendInfo appendInfo, Integer id) {
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
