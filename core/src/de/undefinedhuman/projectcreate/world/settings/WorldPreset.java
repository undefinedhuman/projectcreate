package de.undefinedhuman.projectcreate.world.settings;

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

}
