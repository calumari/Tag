package net.socialhangover.tag.config;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.socialhangover.tag.config.ConfigKeyTypes.customKey;

public class ConfigKeys {

    private static final Map<String, ConfigKey<?>> KEYS;
    private static final int SIZE;

    public static ConfigKey<Long> TAG_COOLDOWN = customKey(c -> c.getLong("cooldown", 5000));

    public static ConfigKey<String> DEFAULT_SKULL_OWNER = customKey(c -> c.getString("skull-owner", "MHF_Villager"));
    public static ConfigKey<Boolean> ENABLE_EASTER_EGG = customKey(c -> c.getBoolean("easter-egg", false));

    static {
        Map<String, ConfigKey<?>> keys = new LinkedHashMap<>();
        Field[] values = ConfigKeys.class.getFields();
        int i = 0;

        for (Field f : values) {
            if (!Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            if (!ConfigKey.class.equals(f.getType())) {
                continue;
            }

            try {
                ConfigKeyTypes.BaseConfigKey<?> key = (ConfigKeyTypes.BaseConfigKey<?>) f.get(null);
                key.ordinal = i++;
                keys.put(f.getName(), key);
            } catch (Exception e) {
                throw new RuntimeException("Exception processing field: " + f, e);
            }
        }

        KEYS = ImmutableMap.copyOf(keys);
        SIZE = i;
    }

    private ConfigKeys() {}

    public static Map<String, ConfigKey<?>> getKeys() {
        return KEYS;
    }

    public static int size() {
        return SIZE;
    }

}
