package xyz.lotho.me.bedwars.generators;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.util.Chat;

public class Generator {

    private final Bedwars instance;
    private final Game game;
    private final Location spawnLocation;
    private final GeneratorType generatorType;
    private final GeneratorTier generatorTier = GeneratorTier.ONE;
    private final boolean isIsland;

    private ArmorStand armorStand;

    private int secondsSinceActivation = 0;

    public Generator(Bedwars instance, Game game, Location spawnLocation, GeneratorType generatorType, boolean isIsland) {
        this.instance = instance;
        this.game = game;
        this.spawnLocation = spawnLocation;
        this.generatorType = generatorType;
        this.isIsland = isIsland;

        if (!isIsland) setActive();
    }

    public void spawnMaterials() {
        if (secondsSinceActivation < this.getSpawnRate()) {
            secondsSinceActivation++;

            if (!isIsland) this.updateArmorStand();
            return;
        }

        secondsSinceActivation = 1;

        this.game.getGameWorld().dropItem(this.spawnLocation, new ItemStack(this.generatorType.getMaterialType()));
        this.game.getGameWorld().playSound(this.spawnLocation, Sound.CHICKEN_EGG_POP, 1, 2);
    }

    public double getSpawnRate() {
        if (generatorType == GeneratorType.EMERALD) {
            if (generatorTier == GeneratorTier.ONE) {
                return 60;
            }
            else if (generatorTier == GeneratorTier.TWO) {
                return 30;
            }
        }
        else if (generatorType == GeneratorType.DIAMOND) {
            if (generatorTier == GeneratorTier.ONE) {
                return 20;
            }
            else if (generatorTier == GeneratorTier.TWO) {
                return 10;
            }
        }
        else if (generatorType == GeneratorType.GOLD) {
            return 4.5;
        }

        return 1;
    }

    private void setActive() {
        armorStand = this.game.getGameWorld().spawn(this.spawnLocation.add(0.5, 2, 0.5), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setGravity(false);
    }

    private String getArmorStandName() {
        int timeLeft = (int) this.getSpawnRate() - this.secondsSinceActivation;
        return "&a" + timeLeft + " second" + (timeLeft == 1 ? "" : "s");
    }

    private void updateArmorStand() {
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(Chat.color(getArmorStandName()));
        armorStand.setItemInHand(new ItemStack(this.generatorType.getDisplayMaterial()));
    }

    public boolean isIsland() {
        return isIsland;
    }
}
