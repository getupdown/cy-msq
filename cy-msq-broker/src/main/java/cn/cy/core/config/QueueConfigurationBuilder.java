package cn.cy.core.config;

import java.nio.file.Paths;

/**
 * 队列配置构造器,全部当做string处理, 方便yaml解析
 */
public class QueueConfigurationBuilder {

    private Integer maxMsgPerFile;

    private String namingPrefix;

    private String msgRootPath;

    public QueueConfigurationBuilder setMaxMsgPerFile(Integer maxMsgPerFile) {
        this.maxMsgPerFile = maxMsgPerFile;
        return this;
    }

    public QueueConfigurationBuilder setNamingPrefix(String namingPrefix) {
        this.namingPrefix = namingPrefix;
        return this;
    }

    public QueueConfigurationBuilder setMsgRootPath(String msgRootPath) {
        this.msgRootPath = msgRootPath;
        return this;
    }

    public QueueConfiguration build() {

        return new QueueConfiguration(
                maxMsgPerFile,
                namingPrefix,
                Paths.get(msgRootPath)
        );
    }
}
