package net.socialhangover.tag.config;

import org.bukkit.configuration.file.YamlConfiguration;

public interface ConfigKey<T> {
    int ordinal();

    T get(YamlConfiguration config);
}
