package net.jadedmc.elytrapvp.utils.item;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class FireworkBuilder extends ItemBuilder {
    private int power;
    private List<FireworkEffect> effects = new ArrayList<>();

    public FireworkBuilder() {
        super(Material.FIREWORK_ROCKET);
    }

    public FireworkBuilder addEffect(FireworkEffect effect) {
        effects.add(effect);
        return this;
    }

    public FireworkBuilder setPower(int power) {
        this.power = power;
        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack item = super.build();
        FireworkMeta meta = (FireworkMeta) item.getItemMeta();

        meta.setPower(power);
        meta.addEffects(effects);
        item.setItemMeta(meta);
        return item;
    }
}