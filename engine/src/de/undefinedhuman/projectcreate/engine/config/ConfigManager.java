package de.undefinedhuman.projectcreate.engine.config;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.core.engine.file.*;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

public class ConfigManager extends Manager implements Serializable {

    public static ConfigManager instance;
    private FsFile file;

    public ConfigManager() {
        if (instance == null) instance = this;
    }

    @Override
    public void init() {
        String fileName = "game.config";
        file = new FsFile(Paths.CONFIG_PATH, fileName, Files.FileType.External);
        if (file.length() != 0) load();
    }

    @Override
    public void delete() {
        save();
    }

    @Override
    public void save() {
        FileWriter writer = file.getFileWriter(true);
        try {
            for (Setting setting : SettingsManager.instance.getSettings()) writer.writeString(setting.getKey()).writeValue(setting.getValue()).nextLine();
            writer.close();
            Log.info("Config file was saved successfully!");
        } catch (Exception ex) {
            Log.error("Error while saving the config \n" + ex.getMessage());
        }
    }

    @Override
    public void load() {
        try {
            FileReader reader = file.getFileReader(true);
            while (reader.nextLine() != null) {
                String key = reader.getNextString(), value = reader.getNextString();
                for (Setting setting : SettingsManager.instance.getSettings())
                    if (setting.getKey().equalsIgnoreCase(key)) setting.setValue(value);
            }
            reader.close();
            Log.info("Config file was loaded successfully!");
        } catch (Exception ex) {
            Log.error("Error while loading the config file.");
            Log.error(ex.getMessage());
        }
    }

}
