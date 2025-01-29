package it.unibs.ingswproject.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Nicolò Rebaioli
 */
public abstract class YamlUtils {
    public static Map<String, Object> readYaml(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            return new Yaml().load(fileReader);
        }
    }

    public static void writeYaml(File file, Map<String, Object> data) throws IOException {
        // Set style
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setSplitLines(false);
        options.setAllowReadOnlyProperties(true);
        options.setAllowUnicode(true);
        options.setLineBreak(DumperOptions.LineBreak.UNIX);
        Yaml yaml = new Yaml(options);

        try (FileWriter fileWriter = new FileWriter(file)) {
            yaml.dump(data, fileWriter);
        }
    }
}
