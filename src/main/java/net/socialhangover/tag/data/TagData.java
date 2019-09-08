package net.socialhangover.tag.data;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TagData {

    private final File file;
    private final YamlConfiguration configuration;

    @Getter
    @Nullable
    private UUID lastTagged;

    @Getter
    private Long lastTaggedTime;

    @Getter
    @Nullable
    private UUID tagged;

    public TagData(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.file = file;
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public synchronized void load() {
        lastTagged = getUuid("last");
        lastTaggedTime = configuration.getLong("time");
        tagged = getUuid("player");
    }

    @Nullable
    private UUID getUuid(String path) {
        String value = configuration.getString(path);
        return value == null ? null : UUID.fromString(value);
    }

    public void setLastTagged(UUID value) {
        configuration.set("last", value.toString());
        lastTagged = value;
    }

    public void setLastTaggedTime() {
        setLastTaggedTime(System.currentTimeMillis());
    }

    public void setLastTaggedTime(long value) {
        configuration.set("time", value);
        lastTaggedTime = value;
    }

    public void setTagged(UUID value) {
        configuration.set("player", value.toString());
        tagged = value;
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
