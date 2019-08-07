package net.socialhangover.tag.listeners;

import lombok.RequiredArgsConstructor;
import net.socialhangover.tag.TagPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final TagPlugin plugin;

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.getConfiguration().TAGGED_PLAYER.get() == null) {
            plugin.tag(event.getPlayer());
            PlayerJoinEvent.getHandlerList().unregister(this);
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (plugin.isTagged(event.getPlayer())) { plugin.applyTag(event.getPlayer()); }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (!plugin.isTagged(damager) || !plugin.canTag()) { return; }
            plugin.tag((Player) event.getEntity());
            plugin.removeTag(damager);
            event.setDamage(0);
        }
    }

}
