package net.jadedmc.elytrapvp.utils.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class PotionBuilder extends ItemBuilder {
    private List<PotionEffect> effects = new ArrayList<>();

    public PotionBuilder(Material material, int amount) {
        super(material, amount);
    }

    public PotionBuilder addEffect(PotionEffect effect) {
        effects.add(effect);
        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack item = super.build();
        PotionMeta meta = (PotionMeta) item.getItemMeta();

        effects.forEach(effect -> meta.addCustomEffect(effect, true));
        item.setItemMeta(meta);

        return item;
    }
}