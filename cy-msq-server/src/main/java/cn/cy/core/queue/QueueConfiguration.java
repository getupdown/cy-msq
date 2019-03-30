package cn.cy.core.queue;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 队列配置
 * <p>
 * 一个队列 -> 一个配置对象
 */
public class QueueConfiguration {

    // 每个文件最多存储多少条消息
    public final Integer MAX_MSG_PER_FILE = 100000;

    // 每个索引文件最多存储多少条索引
    public final Integer MAX_INDEX_ITEM_PER_FILE = 100000;

    // 命名格式
    public final String namingPrefix = "test_queue";

    public final Path msgRootPath = Paths.get("test");

    public final String queueName = "test_queue";
}
