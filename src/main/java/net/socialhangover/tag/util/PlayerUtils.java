package net.socialhangover.tag.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;

public class PlayerUtils {
    private final static Random R = new Random();

    private PlayerUtils() {
    }

    public static boolean addItemsOrDrop(Inventory inventory, Player player, ItemStack... items) {
        Map<Integer, ItemStack> returned = inventory.addItem(items);
        if (!returned.isEmpty()) {
            addItemsOrDrop(player, returned.values().toArray(new ItemStack[0]));
        }
        return !returned.isEmpty();
    }

    public static boolean addItemsOrDrop(Player player, ItemStack... items) {
        Map<Integer, ItemStack> returned = player.getInventory().addItem(items);
        for (ItemStack item : returned.values()) {
            dropNotchian(player, item);
        }
        return !returned.isEmpty();
    }

    public static void dropNotchian(Player player, ItemStack item) {
        Location l = player.getEyeLocation();

        double var5 = 0.3;
        double x = -Math.sin(l.getYaw() / 180.0 * Math.PI) * Math.cos(l.getPitch() / 180.0 * Math.PI) * var5;
        double z = Math.cos(l.getYaw() / 180.0 * Math.PI) * Math.cos(l.getPitch() / 180.0 * Math.PI) * var5;
        double y = -Math.sin(l.getPitch() / 180.0 * Math.PI) * var5 + 0.1;
        var5 = 0.02;
        double var6 = R.nextDouble() * Math.PI * 2.0;
        var5 *= R.nextDouble();
        x += Math.cos(var6) * var5;
        y += (R.nextDouble() - R.nextDouble()) * 0.1;
        z += Math.sin(var6) * var5;

        player.getWorld().dropItem(l, item).setVelocity(new Vector(x, y, z));
    }
}
