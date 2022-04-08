package xyz.lotho.me.bedwars.managers.map;

import xyz.lotho.me.bedwars.Bedwars;

import java.io.File;
import java.util.ArrayList;

public class MapManager {

    private final Bedwars instance;

    public MapManager(Bedwars instance) {
        this.instance = instance;
    }

    public ArrayList<String> getMaps() {
        ArrayList<String> mapNames = new ArrayList<>();

        File directory = new File(this.instance.getDataFolder().getAbsolutePath() + "/schematics/");
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("[ERROR] No maps found in schematics directory.");
            return mapNames;
        }

        for (File file : files) {
            if (file.isFile()) {
                mapNames.add(file.getName().split("\\.")[0]);
            }
        }

        return mapNames;
    }
}
