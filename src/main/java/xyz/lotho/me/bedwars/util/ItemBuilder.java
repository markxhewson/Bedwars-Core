package xyz.lotho.me.bedwars.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder addAllItemFlags() {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(Chat.color(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short id) {
        this.item.setDurability(id);
        return this;
    }

    public ItemBuilder setArmorColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.item.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.item.setItemMeta(leatherArmorMeta);
        return this;
    }

    public ItemBuilder setLore(String... lines) {
        ItemMeta meta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>();

        for (String line : lines) {
            lore.add(Chat.color(line));
        }

        meta.setLore(lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String line) {
        ItemMeta meta = this.item.getItemMeta();
        List<String> lore;

        if (meta.getLore() == null) lore = new ArrayList<>();
        else lore = meta.getLore();

        lore.add(Chat.color(line));
        meta.setLore(lore);
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(flag);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.item.getItemMeta();
        if(unbreakable) {
            meta.spigot().setUnbreakable(true);
        } else {
            meta.spigot().setUnbreakable(false);
        }

        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addUnsafeEnchant(Enchantment ench, int level) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(ench, level, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public static boolean hasItemName(String name, ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return false;
        }

        if(item.hasItemMeta()) {
            if(item.getItemMeta().hasDisplayName()) {
                if(item.getItemMeta().getDisplayName().equals(name)) {
                    return true;
                }
            }
        }

        return false;
    }

}
