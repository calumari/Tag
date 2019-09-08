package net.socialhangover.tag.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.function.Function;

public final class ConfigKeyTypes {

    public static <T> CustomKey<T> customKey(Function<YamlConfiguration, T> function) {
        return new CustomKey<>(function);
    }

    public abstract static class BaseConfigKey<T> implements ConfigKey<T> {
        int ordinal = -1;

        BaseConfigKey() {}

        @Override
        public int ordinal() {
            return this.ordinal;
        }
    }

    public static class CustomKey<T> extends BaseConfigKey<T> {
        private final Function<YamlConfiguration, T> function;

        private CustomKey(Function<YamlConfiguration, T> function) {
            this.function = function;
        }

        @Override
        public T get(YamlConfiguration config) {
            return this.function.apply(config);
        }
    }
}
