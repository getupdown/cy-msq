package cn.cy.core.persistence.file.msg;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import cn.cy.core.config.QueueConfiguration;

/**
 * 创建消息文件的工厂
 */
public class MessageFileFactory {

    private final QueueConfiguration queueConfiguration;

    public MessageFileFactory(QueueConfiguration queueConfiguration) {
        this.queueConfiguration = queueConfiguration;
    }

    /**
     * 工厂把文件载入内存
     *
     * @param id
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    public MessageFile loadMessageFileIntoMemory(Integer id) throws FileNotFoundException {

        return new MessageFile(constructFilePath(String.valueOf(id), queueConfiguration.msgRootPath), id, true);
    }

    /**
     * 工厂创建文件,并载入内存
     *
     * @param id
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    public MessageFile buildMessageFile(Integer id) {

        try {
            return new MessageFile(constructFilePath(String.valueOf(id), queueConfiguration.msgRootPath), id, false);
        } catch (FileNotFoundException e) {
            // ignore
        }
        return null;
    }

    private Path constructFilePath(String newId, Path msgRootPath) {
        return Paths.get(msgRootPath.toString() + "/" + queueConfiguration.namingPrefix + "_" + newId + ".log");
    }

}
