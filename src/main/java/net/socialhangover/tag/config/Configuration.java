package net.socialhangover.tag.config;

import net.socialhangover.tag.TagPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private final File file;
    private Object[] values = null;
    private YamlConfiguration configuration;

    public Configuration(TagPlugin plugin, File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }
        this.file = file;
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public synchronized void load() {
        values = new Object[ConfigKeys.size()];
        for (ConfigKey<?> key : ConfigKeys.getKeys().values()) {
            Object value = key.get(this.configuration);
            this.values[key.ordinal()] = value;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ConfigKey<T> key) {
        return (T) this.values[key.ordinal()];
    }

}
