package xyz.lotho.me.bedwars.managers.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.bedwars.Bedwars;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

public class Disguise {
    private final Bedwars instance;

    private String name;
    private String texture;
    private String signature;

    private String originalName;
    private String originalTexture;
    private String originalSignature;

    public Disguise(Bedwars instance, String name, String texture, String signature) {
        this.instance = instance;
        this.setName(name);
        this.setTexture(texture);
        this.setSignature(signature);
    }

    public boolean apply(Player player) {
        GameProfile gameProfile = null;

        try {
            gameProfile = this.instance.getNmsHelper().getGameProfile(player);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (gameProfile == null) {
            return false;
        }

        this.setOriginalName(player.getName());
        Property originalTextures = this.instance.getNmsHelper().getTexturesProperty(gameProfile);
        if (originalTextures != null) {
            this.setOriginalTexture(originalTextures.getValue());
            this.setOriginalSignature(originalTextures.getSignature());
        }

        gameProfile.getProperties().clear();
        gameProfile.getProperties()
                .put("textures",
                        new Property(
                                "textures",
                                getTexture(),
                                getSignature()
                        ));

        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(gameProfile, getName());
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (getName() != null) player.setDisplayName(getName());

        Bukkit.getOnlinePlayers().forEach(all -> {
            all.hidePlayer(player);
            all.showPlayer(player);
        });

        Location location = player.getLocation();

        CraftPlayer craftPlayer = (CraftPlayer) player;
        CraftWorld craftWorld = (CraftWorld) player.getWorld();

        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, craftPlayer.getHandle()));
        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, craftPlayer.getHandle()));
        craftPlayer.getHandle().playerConnection.sendPacket(
                new PacketPlayOutRespawn(
                        (byte) craftPlayer.getWorld().getEnvironment().getId(),
                        craftWorld.getHandle().getDifficulty(),
                        craftWorld.getHandle().getWorldData().getType(),
                        craftPlayer.getHandle().playerInteractManager.getGameMode()
                )
        );

        player.updateInventory();
        player.teleport(location);

        return true;
    }

    public boolean remove(Player player) {
        this.setName(getOriginalName());
        this.setTexture(getOriginalTexture());
        this.setSignature(getOriginalSignature());
        return apply(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOriginalTexture() {
        return originalTexture;
    }

    public void setOriginalTexture(String originalTexture) {
        this.originalTexture = originalTexture;
    }

    public String getOriginalSignature() {
        return originalSignature;
    }

    public void setOriginalSignature(String originalSignature) {
        this.originalSignature = originalSignature;
    }
}
