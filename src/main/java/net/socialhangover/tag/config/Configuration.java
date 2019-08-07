package net.socialhangover.tag.config;

import lombok.Getter;
import net.socialhangover.tag.TagPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Configuration {

    private final TagPlugin plugin;
    private final File file;
    private YamlConfiguration config;

    public final LazyConfigKey<Long> TAG_COOLDOWN = new LazyConfigKey<Long>() {
        @Override
        public Long get() {
            return config.getLong("cooldown", 60000);
        }

        @Override
        public void set(Long value) { }
    };

    public final ConfigKey<UUID> TAGGED_PLAYER = new ConfigKey<UUID>() {
        @Override
        public UUID get() {
            String value = config.getString("tagged.player");
            return value == null ? null : UUID.fromString(value);
        }

        @Override
        public void set(UUID value) {
            ConfigurationSection section = config.getConfigurationSection("tagged");
            if (section == null) section = config.createSection("tagged");
            section.set("player", value.toString());
        }
    };

    public final ConfigKey<UUID> TAGGED_LAST = new ConfigKey<UUID>() {
        @Override
        public UUID get() {
            String value = config.getString("tagged.last");
            return value == null ? null : UUID.fromString(value);
        }

        @Override
        public void set(UUID value) {
            ConfigurationSection section = config.getConfigurationSection("tagged");
            if (section == null) section = config.createSection("tagged");
            section.set("last", value.toString());
        }
    };

    public final ConfigKey<Long> TAGGED_TIME = new ConfigKey<Long>() {

        @Override
        public Long get() {
            return config.getLong("tagged.time");
        }

        @Override
        public void set(Long value) {
            ConfigurationSection section = config.getConfigurationSection("tagged");
            if (section == null) section = config.createSection("tagged");
            section.set("time", System.currentTimeMillis());
        }
    };

    public Configuration(TagPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract class LazyConfigKey<T> extends ConfigKey<T> {
        @Getter(lazy = true)
        private final T value = get();
    }

}
