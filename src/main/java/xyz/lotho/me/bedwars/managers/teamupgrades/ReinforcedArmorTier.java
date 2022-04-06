package xyz.lotho.me.bedwars.managers.teamupgrades;

public enum ReinforcedArmorTier {

    NONE("Reinforced Armor 0", 0, 0),
    I("Reinforced Armor I", 4, 1),
    II("Reinforced Armor II", 8, 2),
    III("Reinforced Armor III", 12, 3),
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
}
