package cn.cy.core.persistence.dispatch;

import java.util.List;

import cn.cy.core.config.QueueConfiguration;
import cn.cy.core.persistence.file.QueueMsgFile;

public class MessageFileWriteDispatcherDebug extends MessageFileWriteDispatcher {

    public MessageFileWriteDispatcherDebug(QueueConfiguration queueConfiguration) {
        super(queueConfiguration);
    }

    public MessageFileWriteDispatcherDebug(List<QueueMsgFile> messageFiles,
                                           QueueConfiguration queueConfiguration) {
        super(messageFiles, queueConfiguration);
    }

    public MessageFileWriteDispatcherDebug(List<QueueMsgFile> messageFiles, int writeIndex,
                                           QueueConfiguration queueConfiguration) {
        super(messageFiles, writeIndex, queueConfiguration);
    }

    public List<QueueMsgFile> getMsgFiles() {
        return messageFiles;
    }

    @Override
    protected QueueMsgFile createNewMsgFile() {
        return new MessageFileWriteDispatcherTest.MockMsgFile(writeIndex);
    }
}
