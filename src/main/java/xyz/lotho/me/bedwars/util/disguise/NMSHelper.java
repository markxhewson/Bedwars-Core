package xyz.lotho.me.bedwars.util.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class NMSHelper {

    public Object getHandle(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return player.getClass().getMethod("getHandle").invoke(player);
    }

    public GameProfile getGameProfile(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object handle = getHandle(player);

        return (GameProfile) handle.getClass()
                .getSuperclass()
                .getDeclaredMethod("getProfile")
                .invoke(handle);
    }

    public Property getTexturesProperty(GameProfile profile) {
        Optional<Property> texturesProperty = profile.getProperties().get("textures").stream().findFirst();
        return texturesProperty.orElse(null);
    }
}
