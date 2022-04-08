package xyz.lotho.me.bedwars.managers.teamupgrades;

public enum ReinforcedArmorTier {

    NONE("Reinforced Armor 0", 0, 0),
    I("Reinforced Armor I", 2, 1),
    II("Reinforced Armor II", 4, 2),
    III("Reinforced Armor III", 8, 3),
    IV("Reinforced Armor IV", 16, 4);

    private final String formattedName;
    private final int diamondsRequired;
    private final int level;

    private ReinforcedArmorTier(String formattedName, int diamondsRequired, int level) {
        this.formattedName = formattedName;
        this.diamondsRequired = diamondsRequired;
        this.level = level;
    }

    public String getFormattedName() {
        return this.formattedName;
    }

    public int getDiamondsRequired() {
        return this.diamondsRequired;
    }

    public int getLevel() {
        return this.level;
    }

    public ReinforcedArmorTier getNext() {
        if (this == ReinforcedArmorTier.NONE) return ReinforcedArmorTier.I;
        else if (this == ReinforcedArmorTier.I) return ReinforcedArmorTier.II;
        else if (this == ReinforcedArmorTier.II) return ReinforcedArmorTier.III;
        else if (this == ReinforcedArmorTier.III) return ReinforcedArmorTier.IV;
        else return ReinforcedArmorTier.IV;
    }
}
