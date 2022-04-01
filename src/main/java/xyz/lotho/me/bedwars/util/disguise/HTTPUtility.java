package xyz.lotho.me.bedwars.util.disguise;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import org.bukkit.Bukkit;
import xyz.lotho.me.bedwars.Bedwars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class HTTPUtility {
    private final Bedwars instance;
    private final Cache<String, String[]> cachedSkinResponses = CacheBuilder.newBuilder()
            .expireAfterWrite(5L, TimeUnit.MINUTES)
            .build();

    public HTTPUtility(Bedwars instance) {
        this.instance = instance;
    }

    private interface JSONResponseCallback {
        void handle(JsonObject response);
    }

    private interface UUIDResponseCallback {
        void handle(String uuid);
    }

    public interface GetTextureResponse {
        void handle(String texture, String signature);
    }

    public void getTextureAndSignature(String playerName, GetTextureResponse response) {
        String[] previousResponse = cachedSkinResponses.getIfPresent(playerName);
        if (previousResponse != null) {
            response.handle(previousResponse[0], previousResponse[1]);
            return;
        }

        getUUIDForPlayerName(playerName, (uuid -> {
            if (uuid == null) {
                response.handle(null, null);
                return;
            }

            getTextureAndSignatureFromUUID(uuid, ((texture, signature) -> {
                cachedSkinResponses.put(playerName, new String[]{texture, signature});
                response.handle(texture, signature);
            }));
        }));
    }

    public void getUUIDForPlayerName(String playerName, UUIDResponseCallback response) {
        get("https://api.mojang.com/users/profiles/minecraft/" + playerName, (uuidReply) -> {
            if (uuidReply == null) {
                response.handle(null);
                return;
            }

            String uuidString = uuidReply.get("id").getAsString();
            if (uuidString == null) {
                response.handle(null);
                return;
            }

            response.handle(uuidString);
        });
    }

    public void getTextureAndSignatureFromUUID(String uuidString, GetTextureResponse response) {
        get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString + "?unsigned=false", (profileReply) -> {
            if (!profileReply.has("properties")) {
                response.handle(null, null);
                return;
            }

            JsonObject properties = (JsonObject) profileReply.get("properties").getAsJsonArray().get(0);
            String texture = properties.get("value").getAsString();
            String signature = properties.get("signature").getAsString();
            response.handle(texture, signature);
        });
    }

    private void get(String url, JSONResponseCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
            try {
                URL rawURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) rawURL.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                if (content.toString().isEmpty()) { // Mojang API 204 fix
                    Bukkit.getScheduler().runTask(this.instance, () -> callback.handle(null));
                    return;
                }

                JsonObject jsonObject = (JsonObject) new JsonParser().parse(content.toString());

                Bukkit.getScheduler().runTask(this.instance, () -> callback.handle(jsonObject));
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getScheduler().runTask(this.instance, () -> callback.handle(null));
            }
        });
    }
}
