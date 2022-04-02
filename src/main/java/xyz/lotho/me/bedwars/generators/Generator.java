package xyz.lotho.me.bedwars.generators;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

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

        secondsSinceActivation = 0;

        this.game.getWorld().dropItem(this.spawnLocation, new ItemStack(this.generatorType.getMaterialType()));
        this.game.getWorld().playSound(this.spawnLocation, Sound.CHICKEN_EGG_POP, 1, 2);
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
        armorStand = this.game.getWorld().spawn(this.spawnLocation.add(0.5, 2, 0.5), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setGravity(false);

        Location loc = armorStand.getLocation();

        new BukkitRunnable() {
            double radius = 2;
            double angle = 0;

            @Override
            public void run() {
                final double x = radius * Math.cos(angle);
                final double z = radius * Math.sin(angle);
                angle += Math.PI / 36;

                loc.add(x, 0, z);
                look(armorStand, loc);
                loc.subtract(x, 0, z);
            }

        }.runTaskTimer(this.instance, 0, 0);
    }

    private void look(ArmorStand stand, Location loc) {
        Location lookDir = loc.subtract(stand.getLocation());
        EulerAngle poseAngle = new EulerAngle(0, -Math.atan2(lookDir.getX(), lookDir.getZ()) + Math.PI / 4, 0);
        stand.setHeadPose(poseAngle);
        lookDir.add(stand.getLocation());
    }

    private String getArmorStandName() {
        int timeLeft = (int) this.getSpawnRate() - this.secondsSinceActivation;
        return "&a" + timeLeft + " second" + (timeLeft == 1 ? "" : "s");
    }

    private void updateArmorStand() {
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(Chat.color(getArmorStandName()));
        armorStand.setHelmet(new ItemBuilder(this.generatorType.getDisplayMaterial()).build());
    }

    public boolean isIsland() {
        return isIsland;
    }
}
