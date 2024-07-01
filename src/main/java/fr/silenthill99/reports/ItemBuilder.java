package fr.silenthill99.reports;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@SuppressWarnings({"MethodDoesntCallSuperMethod", "deprecation"})
public class ItemBuilder {
    private final ItemStack is;
    private final ItemMeta im;

    public ItemBuilder(ItemStack is) {
        this.is = is;
        this.im = is.getItemMeta();
    }

    public ItemBuilder(Material m, int amount) {
        this.is = new ItemStack(m, amount);
        this.im = is.getItemMeta();
    }

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder setName(String name) {
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        im.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }
}
