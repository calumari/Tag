package net.socialhangover.tag;

import lombok.Getter;
import net.socialhangover.tag.config.ConfigKeys;
import net.socialhangover.tag.config.Configuration;
import net.socialhangover.tag.data.TagData;
import net.socialhangover.tag.listeners.PlayerListener;
import net.socialhangover.tag.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class TagPlugin extends JavaPlugin {

    private static final UUID EASTER_EGG = UUID.fromString("8fd5774a-fe35-44ba-9b9a-c4d29291d398");
    private static final ItemStack TAGGED_ITEM = new ItemStack(Material.PLAYER_HEAD);
    private static final String TAGGED_ITEM_NAME = "TAGGED";

    @Getter
    private Configuration configuration;

    @Getter
    private TagData data;

    @Override
    public void onEnable() {
        configuration = new Configuration(this, new File(getDataFolder(), "config.yml"));
        configuration.load();

        data = new TagData(new File(getDataFolder(), "data.yml"));
        data.load();

        SkullMeta meta = (SkullMeta) TAGGED_ITEM.getItemMeta();
        meta.setDisplayName(TAGGED_ITEM_NAME);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        meta.setOwner(configuration.get(ConfigKeys.DEFAULT_SKULL_OWNER)); // TODO: Deprecated. setOwningPlayer doesn't seem to work.
        TAGGED_ITEM.setItemMeta(meta);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public boolean isTagged(Player player) {
        return player.getUniqueId().equals(data.getTagged());
    }

    public boolean canTag(Player player) {
        return System.currentTimeMillis() - data.getLastTaggedTime() > configuration.get(ConfigKeys.TAG_COOLDOWN) && //
                (!player.getUniqueId().equals(data.getLastTagged()) || Bukkit.getOnlinePlayers().size() == 2);
    }

    public void tag(Player player) {
        if (configuration.get(ConfigKeys.ENABLE_EASTER_EGG) && player.getUniqueId().equals(EASTER_EGG)) {
            SkullMeta meta = (SkullMeta) TAGGED_ITEM.getItemMeta();
            //noinspection deprecation
            meta.setOwner("MHF_Cow");
            TAGGED_ITEM.setItemMeta(meta);
        }
        data.setTagged(player.getUniqueId());
        data.setLastTaggedTime();
        data.save();
        applyTag(player);
    }

    public void tag(Player from, Player to) {
        data.setLastTagged(from.getUniqueId());
        removeTag(from);

        SkullMeta meta = (SkullMeta) TAGGED_ITEM.getItemMeta();
        if (configuration.get(ConfigKeys.ENABLE_EASTER_EGG) && to.getUniqueId().equals(EASTER_EGG)) {
            meta.setOwner("MHF_Cow");
        } else {
            meta.setOwningPlayer(from);
        }
        meta.setLore(Arrays.asList("You were tagged by " + from.getDisplayName()));
        TAGGED_ITEM.setItemMeta(meta);

        data.setTagged(to.getUniqueId());
        data.setLastTaggedTime();
        data.save();
        applyTag(to);
    }

    public boolean hasTag(Player player) {
        ItemStack item = player.getInventory().getHelmet();
        if (item == null || !item.hasItemMeta()) { return false; }
        ItemMeta meta = item.getItemMeta();
        return meta.getDisplayName().equalsIgnoreCase(TAGGED_ITEM_NAME);
    }

    public void applyTag(Player player) {
        ItemStack item = player.getInventory().getHelmet();
        if (item != null && !item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "TAGGED")) {
            PlayerUtils.addItemsOrDrop(player, item);
        }
        player.getInventory().setHelmet(TAGGED_ITEM);
    }

    public void removeTag(Player player) {
        if (hasTag(player)) {
            player.getInventory().setHelmet(null);
        }
    }

}
