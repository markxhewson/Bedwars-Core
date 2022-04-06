package xyz.lotho.me.bedwars.managers.teamupgrades;

public enum HasteTier {

    NONE("Haste 0", 0, 0),
    I("Haste 1", 2, 1),
    II("Haste 2", 4, 2);

    private final String formattedName;
    private final int diamondsRequired;
    private final int level;

    private HasteTier(String formattedName, int diamondsRequired, int level) {
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
