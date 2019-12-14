package de.undefinedhuman.sandboxgame.engine.config;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.utils.Manager;

public class ConfigManager extends Manager {

    public static ConfigManager instance;
    private FsFile file;

    public ConfigManager() {
        if(instance == null) instance = this;
    }

    @Override
    public void init() {
        String fileName = "config.txt";
        file = new FsFile(Paths.CONFIG_PATH, fileName, false);
        if(!file.isEmpty()) loadConfig();
    }

    private void loadConfig() {

        try {

            FileReader reader = file.getFileReader(true);

            while(reader.nextLine() != null) {
                String key = reader.getNextString(), value = reader.getNextString();
                for(Setting setting : SettingsManager.instance.settings) if(setting.getName().equalsIgnoreCase(key)) setting.setValue(value);
            }

            reader.close();
            Log.info("Loaded config successfully.");

        } catch(Exception ex) {
            Log.error("Error while loading config.");
            Log.error(ex.getMessage());
        }

    }

    public void saveConfig() {

        FileWriter writer = file.getFileWriter(true);

        try {

            for(Setting setting : SettingsManager.instance.settings) {
                writer.writeString(setting.getName());
                writer.writeValue(setting.getValue());
                writer.nextLine();
            }

            writer.close();
            Log.info("Saved config successfully!");

        } catch(Exception ex) {
            Log.error("Error while saving config!");
            Log.error(ex.getMessage());
        }

    }

    @Override
    public void delete() {
        saveConfig();
    }

}
