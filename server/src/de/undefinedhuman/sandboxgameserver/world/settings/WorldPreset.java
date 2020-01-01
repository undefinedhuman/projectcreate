package de.undefinedhuman.sandboxgameserver.world.settings;

import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;

public class WorldPreset {

    private String name;
    private WorldSetting worldSetting;
    private BiomeSetting biomeSetting;

    public WorldPreset(String name, WorldSetting worldSetting, BiomeSetting biomeSetting) {

        this.name = name;
        this.worldSetting = worldSetting;
        this.biomeSetting = biomeSetting;

    }

    public String getName() {
        return name;
    }

    public WorldSetting getWorldSetting() {
        return worldSetting;
    }

    public BiomeSetting getBiomeSetting() {
        return biomeSetting;
    }

    public void save(FileWriter writer) {

        writer.writeString(name);
        writer.writeString(worldSetting.name());
        writer.writeString(biomeSetting.name());

    }

    public void load(FileReader reader) {

        this.name = reader.getNextString();
        this.worldSetting = WorldSetting.valueOf(reader.getNextString());
        this.biomeSetting = BiomeSetting.valueOf(reader.getNextString());

    }

}
