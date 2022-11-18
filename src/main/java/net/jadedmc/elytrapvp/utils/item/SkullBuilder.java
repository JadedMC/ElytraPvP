package net.jadedmc.elytrapvp.utils.item;

import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkullBuilder {
    private ItemStack item;
    private SkullMeta meta;

    /**
     * Create a SkullBuilder
     * @param texture Skull Texture
     */
    public SkullBuilder(String texture) {
        UUID id = UUID.nameUUIDFromBytes(texture.getBytes());
        int less = (int) id.getLeastSignificantBits();
        int most = (int) id.getMostSignificantBits();

        item = Bukkit.getUnsafe().modifyItemStack(new ItemStack(Material.PLAYER_HEAD), "{SkullOwner:{Id:[I;" + (less * most) + "," + (less >> 23) + "," + (most / less) + "," + (most * 8731) + "],Properties:{textures:[{Value:\"" + texture + "\"}]}}}");
        meta = (SkullMeta) item.getItemMeta();
    }

    public SkullBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Add an enchantment to the item.
     * @param e Enchantment to add.
     * @param level Level of the enchantment.
     * @return ItemBuilder
     */
    public SkullBuilder addEnchantment(Enchantment e, int level) {
        addEnchantment(e, level, true);
        return this;
    }

    /**
     * Add an enchantment to the item.
     * @param e Enchantment to add.
     * @param level Level of the enchantment.
     * @return ItemBuilder
     */
    public SkullBuilder addEnchantment(Enchantment e, int level, boolean ignore) {
        meta.addEnchant(e, level, ignore);
        return this;
    }

    public SkullBuilder addFlag(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public SkullBuilder addLore(String[] arr, String starter) {
        List<String> lore = meta.getLore();

        if(lore == null) {
            lore = new ArrayList<>();
        }

        for(String str : arr) {
            lore.add(ChatUtils.translate(starter + str));
        }

        meta.setLore(lore);
        return this;
    }

    /**
     * Add lore to the item.
     * @param str String
     * @return ItemBuilder
     */
    public SkullBuilder addLore(String str) {
        List<String> lore = meta.getLore();

        if(lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatUtils.translate(str));
        meta.setLore(lore);

        return this;
    }

    /**
     * Add multiple lines of lore at once.
     * @param arr List of lore.
     * @return ItemBuilder.
     */
    public SkullBuilder addLore(List<String> arr) {
        List<String> lore = meta.getLore();

        if(lore == null) {
            lore = new ArrayList<>();
        }

        for(String str : arr) {
            lore.add(ChatUtils.translate(str));
        }
        meta.setLore(lore);

        return this;
    }
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    /**
     * Set the CustomModelData of the item.
     * @param data Data
     * @return ItemBuilder
     */
    public SkullBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    /**
     * Set the custom durability of the item.
     * @param durability Durability to set
     * @return ItemBuilder
     */
    public SkullBuilder setCustomDurability(int durability) {
        NamespacedKey maxDurability = new NamespacedKey(Bukkit.getPluginManager().getPlugin("CustomItemAPI"), "max-durability");
        NamespacedKey currentDurability = new NamespacedKey(Bukkit.getPluginManager().getPlugin("CustomItemAPI"), "current-durability");

        meta.getPersistentDataContainer().set(maxDurability, PersistentDataType.INTEGER, durability);
        meta.getPersistentDataContainer().set(currentDurability, PersistentDataType.INTEGER, durability);

        return this;
    }

    /**
     * Set the display name of the item.
     * @param str Display name
     * @return ItemBuilder
     */
    public SkullBuilder setDisplayName(String str) {
        meta.setDisplayName(ChatUtils.translate(str));
        return this;
    }

    /**
     * Save persistent data to the item.
     * @param key Key
     * @param value Value
     * @return ItemBuilder
     */
    public SkullBuilder setPersistentData(String key, String value) {
        NamespacedKey namepace = new NamespacedKey(Bukkit.getPluginManager().getPlugin("CustomItemAPI"), key);
        meta.getPersistentDataContainer().set(namepace, PersistentDataType.STRING, value);
        return this;
    }

    /**
     * Save persistent data to the item.
     * @param key Key
     * @param value value
     * @return ItemBuilder
     */
    public SkullBuilder setPersistentData(String key, int value) {
        NamespacedKey namepace = new NamespacedKey(Bukkit.getPluginManager().getPlugin("CustomItemAPI"), key);
        meta.getPersistentDataContainer().set(namepace, PersistentDataType.INTEGER, value);
        return this;
    }
}