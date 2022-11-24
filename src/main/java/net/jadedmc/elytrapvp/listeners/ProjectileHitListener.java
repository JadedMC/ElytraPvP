package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

public class ProjectileHitListener implements Listener {
    private final ElytraPvP plugin;
    private final List<Material> panes = new ArrayList<>();

    public ProjectileHitListener(ElytraPvP plugin) {
        this.plugin = plugin;

        panes.add(Material.GLASS_PANE);
        panes.add(Material.BLACK_STAINED_GLASS_PANE);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {

        // Exit if material is null.
        if(event.getHitBlock() == null) {
            return;
        }

        Material material = event.getHitBlock().getType();

        if(!panes.contains(material)) {
            return;
        }

        breakGlass(event.getEntity(), event.getHitBlock(), material);
    }

    private void breakGlass(Entity entity, Block block, Material material) {
        Projectile projectile = (Projectile) entity;

        if(!(projectile.getShooter() instanceof Player player)) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        customPlayer.addWindowBroken();

        block.setType(Material.AIR);
        projectile.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 3.0F, 0.5F);

        entity.getNearbyEntities(1,1,1).forEach(nearbyEntity -> {
            if(nearbyEntity instanceof Player wittness) {
                if(wittness.getHealth() >= 4) {
                    wittness.setHealth(wittness.getHealth() - 4);
                }
                else {
                    wittness.setHealth(0);
                }
            }
        });

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            block.setType(material);

            getBlocks(block, 1).forEach(surroundingBlock -> {
                BlockData data = surroundingBlock.getBlockData();
                Material type = surroundingBlock.getType();

                surroundingBlock.setType(Material.AIR);
                surroundingBlock.setType(type);
                surroundingBlock.setBlockData(data);
            });
        }, 300);
    }

    private ArrayList<Block> getBlocks(Block start, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        for(double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++){
            for(double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++){
                for(double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }
}