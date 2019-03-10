package cn.cy.core.persistence.dispatch;

import java.util.List;

import cn.cy.core.persistence.file.QueueMsgFile;
import cn.cy.core.queue.QueueConfiguration;

public class MessageFileDispatcherDebug extends MessageFileDispatcher {

    public MessageFileDispatcherDebug(QueueConfiguration queueConfiguration) {
        super(queueConfiguration);
    }

    public MessageFileDispatcherDebug(List<QueueMsgFile> messageFiles,
                                      QueueConfiguration queueConfiguration) {
        super(messageFiles, queueConfiguration);
    }

    public MessageFileDispatcherDebug(List<QueueMsgFile> messageFiles, int writeIndex,
                                      QueueConfiguration queueConfiguration) {
        super(messageFiles, writeIndex, queueConfiguration);
    }

    public List<QueueMsgFile> getMsgFiles() {
        return messageFiles;
    }

    @Override
    protected QueueMsgFile createNewMsgFile() {
        return new MessageFileDispatcherTest.MockMsgFile(writeIndex);
    }
}
