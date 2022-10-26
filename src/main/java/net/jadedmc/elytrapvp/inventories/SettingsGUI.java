package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettingsGUI extends CustomGUI {

    public SettingsGUI(ElytraPvP plugin, Player player) {
        super(54, "Settings");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        setItem(40, new ItemBuilder(Material.ANVIL).setDisplayName("&a&lKit Editor").build(), (p, a) -> new KitEditorGUI(plugin, player).open(p));

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Toggles elytra auto-deploy
        ItemBuilder autoDeploy = new ItemBuilder(Material.ELYTRA).setDisplayName("&a&lAuto Deploy Elytra");
        if(customPlayer.autoDeploy()) {
            autoDeploy.addLore("&aEnabled");
        }
        else {
            autoDeploy.addLore("&cDisabled");
        }
        setItem(19, autoDeploy.build(), (p,a) -> {
            customPlayer.setAutoDeploy(!customPlayer.autoDeploy());
            new SettingsGUI(plugin, p).open(p);
        });

        // Toggles viewing death messages.
        ItemBuilder showAllDeaths = new ItemBuilder(Material.SKELETON_SKULL).setDisplayName("&a&lShow All Death Messages");
        if(customPlayer.showAllDeaths()) {
            showAllDeaths.addLore("&aEnabled");
        }
        else {
            showAllDeaths.addLore("&cDisabled");
        }
        setItem(21, showAllDeaths.build(), (p,a) -> {
            customPlayer.setShowAllDeaths(!customPlayer.showAllDeaths());
            new SettingsGUI(plugin, p).open(p);
        });

        // Toggles particles
        ItemBuilder showParticles = new ItemBuilder(Material.NETHER_STAR).setDisplayName("&a&lShow Particles");
        if(customPlayer.showParticles()) {
            showParticles.addLore("&aEnabled");
        }
        else {
            showParticles.addLore("&cDisabled");
        }
        setItem(23, showParticles.build(), (p,a) -> {
            customPlayer.setShowParticles(!customPlayer.showParticles());
            new SettingsGUI(plugin, p).open(p);
        });

        // Toggles scoreboard
        ItemBuilder scoreboard = new ItemBuilder(Material.OAK_SIGN).setDisplayName("&a&lShow Scoreboard");
        if(customPlayer.showScoreboard()) {
            scoreboard.addLore("&aEnabled");
        }
        else {
            scoreboard.addLore("&cDisabled");
        }
        setItem(25, scoreboard.build(), (p,a) -> {
            customPlayer.setShowScoreboard(!customPlayer.showScoreboard());
            new SettingsGUI(plugin, player).open(player);

            if(customPlayer.showScoreboard()) {
                new GameScoreboard(plugin, p);
            }
        });

    }
}