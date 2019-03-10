package cn.cy.core.persistence.file.msg;

import java.nio.file.Path;
import java.nio.file.Paths;

import cn.cy.core.queue.QueueConfiguration;

/**
 * 创建消息文件的工厂
 */
public class MessageFileFactory {

    private final QueueConfiguration queueConfiguration;

    public MessageFileFactory(QueueConfiguration queueConfiguration) {
        this.queueConfiguration = queueConfiguration;
    }

    public MessageFile buildNewMessageFile(Integer id) {

        MessageFile messageFile =
                new MessageFile(constructFilePath(String.valueOf(id), queueConfiguration.msgRootPath), id);
        return messageFile;
    }

    private Path constructFilePath(String newId, Path msgRootPath) {
        return Paths.get(msgRootPath.toString() + "/" + queueConfiguration.namingPrefix + "_" + newId + ".log");
    }

}
