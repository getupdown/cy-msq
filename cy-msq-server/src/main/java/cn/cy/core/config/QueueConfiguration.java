package cn.cy.core.config;

import java.nio.file.Path;

/**
 * 队列配置
 * <p>
 * 一个队列 -> 一个配置对象
 */
public class QueueConfiguration {

    // 每个文件最多存储多少条消息
    public Integer MAX_MSG_PER_FILE;

    // 命名格式
    public String namingPrefix;

    public Path msgRootPath;

    public QueueConfiguration() {

    }

    public QueueConfiguration(Integer MAX_MSG_PER_FILE, String namingPrefix, Path msgRootPath) {
        this.MAX_MSG_PER_FILE = MAX_MSG_PER_FILE;
        this.namingPrefix = namingPrefix;
        this.msgRootPath = msgRootPath;
    }
}
