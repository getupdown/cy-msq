package cn.cy.utils.yaml;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Yaml转换类
 */
public class YamlUtils {

    public static Map<String, Object> loadYaml(InputStream inputStream) {
        Yaml yaml = new Yaml();

        return yaml.load(inputStream);
    }

    public static Map<String, Object> loadYaml(String rawYaml) {
        Yaml yaml = new Yaml();

        return yaml.load(rawYaml);
    }

    public static <T> T loadYamlAs(String rawYaml, Class<T> type) {
        Yaml yaml = new Yaml();

        return yaml.loadAs(rawYaml, type);
    }

    public static <T> T loadYamlAs(InputStream inputStream, Class<T> type) {
        Yaml yaml = new Yaml();

        return yaml.loadAs(inputStream, type);
    }

}
