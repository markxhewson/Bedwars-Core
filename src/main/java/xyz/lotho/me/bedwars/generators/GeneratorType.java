package xyz.lotho.me.bedwars.generators;

import org.bukkit.Material;

public enum GeneratorType {

    IRON(Material.IRON_INGOT, Material.IRON_BLOCK),
    GOLD(Material.GOLD_INGOT, Material.GOLD_BLOCK),
    DIAMOND(Material.DIAMOND, Material.DIAMOND_BLOCK),
    EMERALD(Material.EMERALD, Material.EMERALD_BLOCK);

    private final Material materialType;
    private final Material displayMaterial;

    private GeneratorType(Material materialType, Material displayMaterial) {
        this.materialType = materialType;
        this.displayMaterial = displayMaterial;
    }

    public Material getMaterialType() {
        return this.materialType;
    }

    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }
}
