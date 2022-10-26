package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KitEditorGUI extends CustomGUI {
    private final ElytraPvP plugin;
    private String kit = "none";

    public KitEditorGUI(ElytraPvP plugin, Player player) {
        super(54, "Kit Editor");
        this.plugin = plugin;

        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        for(int i : fillers) {
            setItem(i, filler);
        }

        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> {
            p.closeInventory();
            new SettingsGUI(plugin, p).open(p);
        });

        int i = 0;
        for(Kit kit : plugin.kitManager().getKits().values()) {
            setItem(i + 9, new ItemBuilder(kit.getIcon())
                            .setDisplayName("&a&l" + kit.getName())
                            .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .addLore("").addLore("&aClick to edit!").build(),
                    (p, a) -> {
                        p.closeInventory();
                        new KitEditorGUI(plugin, p, kit.getId()).open(p);
                    });
            i++;
        }
    }

    public KitEditorGUI(ElytraPvP plugin, Player player, String kit) {
        super(54, "Kit Editor - " + plugin.kitManager().getKit(kit).getName());
        this.plugin = plugin;
        this.kit = kit;
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> {
            p.closeInventory();
            new KitEditorGUI(plugin, p).open(p);
        });

        ItemStack save = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=")
                .setDisplayName("&aSave")
                .build();
        setItem(48, save, (p,a ) -> {
            ItemStack[] items = getInventory().getContents();
            Kit kit1 = plugin.kitManager().getKit(this.kit);
            customPlayer.getKitEditor(kit1.getId()).clear();

            for(int i = 9; i < 45; i++) {
                ItemStack item = items[i];
                if(item == null) {
                    continue;
                }

                if(item.isSimilar(kit1.getItems().get(i))) {
                    continue;
                }

                for(int key : kit1.getItems().keySet()) {
                    if(kit1.getItems().get(key).equals(item)) {
                        customPlayer.getKitEditor(kit1.getId()).put(getReversedMappedSlot(i), key);
                        break;
                    }
                }
            }
            customPlayer.updateKitEditor(kit);
            p.closeInventory();
        });

        ItemStack reset = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==")
                .setDisplayName("&cReset")
                .build();
        setItem(50, reset, (p, a) -> {
            customPlayer.getKitEditor(kit).clear();
            customPlayer.updateKitEditor(kit);
            p.closeInventory();
        });

        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,49,51,52,53};
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        for(int i : fillers) {
            setItem(i, filler);
        }

        Map<Integer, ItemStack> updatedKit = new HashMap<>();
        Set<Integer> slotsUsed = new HashSet<>();

        for(int slot : customPlayer.getKitEditor(kit).keySet()) {
            slotsUsed.add(slot);
            updatedKit.put(slot, plugin.kitManager().getKit(kit).getItems().get(customPlayer.getKitEditor(kit).get(slot)));
        }

        for(int slot : plugin.kitManager().getKit(kit).getItems().keySet()) {
            if(slotsUsed.contains(slot)) {
                continue;
            }

            if(slot > 35) {
                continue;
            }

            ItemStack item =  plugin.kitManager().getKit(kit).getItems().get(slot);

            if(updatedKit.containsValue(item)) {
                continue;
            }

            updatedKit.put(slot, item);
        }

        for(int slot : updatedKit.keySet()) {
            setItem(getMappedSlot(slot), updatedKit.get(slot));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if(item == null) {
            if(event.getWhoClicked().getItemOnCursor().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
        }
        else {
            if(event.getClick().isRightClick() && item.getAmount() > 1) {
                event.setCancelled(true);
                return;
            }

            if(item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.PLAYER_HEAD) {
                if(item.getAmount() > 1) {
                    return;
                }
                event.setCancelled(true);
                return;
            }
        }
    }

    public void onCloses(Player player) {
        if(this.kit.equals("none")) {
            return;
        }

        ItemStack[] items = getInventory().getContents();
        Kit kit = plugin.kitManager().getKit(this.kit);
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        customPlayer.getKitEditor(kit.getId()).clear();

        for(int i = 9; i < 45; i++) {
            ItemStack item = items[i];
            if(item == null) {
                continue;
            }

            if(item.isSimilar(kit.getItems().get(i))) {
                continue;
            }

            for(int key : kit.getItems().keySet()) {
                if(kit.getItems().get(key).equals(item)) {
                    customPlayer.getKitEditor(kit.getId()).put(getReversedMappedSlot(i), key);
                    break;
                }
            }
        }
    }

    private int getMappedSlot(int slot) {
        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i <= 9; i++) {
            map.put(i, (i + 36));
        }

        for(int i = 9; i <= 35; i++) {
            map.put(i, i);
        }

        return map.get(slot);
    }

    private int getReversedMappedSlot(int slot) {
        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i <= 9; i++) {
            map.put((i + 36), i);
        }

        for(int i = 9; i <= 35; i++) {
            map.put(i, i);
        }

        return map.get(slot);
    }
}