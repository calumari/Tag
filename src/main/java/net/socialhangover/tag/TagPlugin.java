package net.socialhangover.tag;

import lombok.Getter;
import net.socialhangover.tag.config.Configuration;
import net.socialhangover.tag.listeners.PlayerListener;
import net.socialhangover.tag.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TagPlugin extends JavaPlugin {

    private static final ItemStack TAGGED_ITEM = new ItemStack(Material.PLAYER_HEAD);

    @Getter
    private Configuration configuration;

    @Override
    public void onEnable() {
        SkullMeta meta = (SkullMeta) TAGGED_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "TAGGED");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        meta.setOwner("MHF_Villager"); // TODO: Deprecated. setOwningPlayer doesn't seem to work.
        TAGGED_ITEM.setItemMeta(meta);

        configuration = new Configuration(this, new File(getDataFolder(), "config.yml"));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public boolean isTagged(Player player) {
        return configuration.TAGGED_PLAYER.get().equals(player.getUniqueId());
    }

    public boolean canTag(Player player) {
        return System.currentTimeMillis() - configuration.TAGGED_TIME.get() > configuration.TAG_COOLDOWN.getValue() && !player
                .getUniqueId()
                .equals(configuration.TAGGED_LAST.get());
    }

    public void tag(Player player) {
        configuration.TAGGED_LAST.set(configuration.TAGGED_PLAYER.get());
        configuration.TAGGED_PLAYER.set(player.getUniqueId());
        configuration.TAGGED_TIME.set(System.currentTimeMillis());
        configuration.save();
        applyTag(player);
    }

    public void applyTag(Player player) {
        ItemStack item = player.getInventory().getHelmet();
        if (item != null && !item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "TAGGED")) {
            PlayerUtils.addItemsOrDrop(player, item);
        }
        player.getInventory().setHelmet(TAGGED_ITEM);
    }

    public void removeTag(Player player) {
        ItemStack item = player.getInventory().getHelmet();
        if (item != null && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "TAGGED")) {
            player.getInventory().setHelmet(null);
        }
    }
}
