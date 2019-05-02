package cn.cy.io.bootstrap;

import java.util.Map;

import cn.cy.core.config.QueueConfiguration;
import cn.cy.core.config.QueueConfigurationBuilder;
import cn.cy.io.handler.test.PersistenceTestHandler;
import cn.cy.utils.yaml.YamlUtils;
import io.netty.channel.socket.SocketChannel;

public class PersistenceTestInitializer extends CustomizeProtocolInitializer {

    @Override
    protected void customizedHandlers(SocketChannel ch) {

        ch.pipeline().addLast(buildTestHandler());

    }

    private PersistenceTestHandler buildTestHandler() {

        return new PersistenceTestHandler(buildConfiguration());

    }

    private QueueConfiguration buildConfiguration() {
        Map<String, Object> yamlMap =
                YamlUtils.loadYaml(PersistenceTestInitializer.class
                        .getClassLoader().getResourceAsStream("queue_config.yaml"));

        return new QueueConfigurationBuilder()
                .setMaxMsgPerFile((Integer) yamlMap.get("max_msg_per_file"))
                .setMsgRootPath((String) yamlMap.get("msg_root_path"))
                .setNamingPrefix((String) yamlMap.get("naming_prefix"))
                .build();
    }
}
