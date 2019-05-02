package cn.cy.core.persistence.file.msg;

public class MappedFileInfo {

    private volatile Long nextWritableOffset = 0L;

    public Long getNextWritableOffset() {
        return nextWritableOffset;
    }

    public void setNextWritableOffset(Long nextWritableOffset) {
        this.nextWritableOffset = nextWritableOffset;
    }
}
