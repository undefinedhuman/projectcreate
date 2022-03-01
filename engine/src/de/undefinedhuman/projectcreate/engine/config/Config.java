package de.undefinedhuman.projectcreate.engine.config;

import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

@SuppressWarnings("ALL")
public abstract class Config extends SettingsList implements Serializable {

    private String fileName;
    private FsFile configFile;
    private boolean base;

    public Config(String fileName) {
        this(fileName, true);
    }

    public Config(String fileName, boolean base) {
        this.fileName = fileName;
        this.base = base;
    }

    public void init() {
        configFile = new FsFile(Paths.getInstance().getDirectory(), Paths.CONFIG_PATH + fileName + ".config");
        if(configFile.exists())
            load();
        save();
    }

    @Override
    public void save() {
        if(FileError.checkFileForErrors("saving Config (" + fileName + ")", configFile, FileError.NULL))
            return;
        FileWriter writer = configFile.getFileWriter(base, ":");
        if(writer == null)
            return;
        Utils.saveSettings(writer, this);
        writer.close();
    }

    @Override
    public void load() {
        if(FileError.checkFileForErrors("loading Config (" + fileName + ")", configFile, FileError.NULL, FileError.NON_EXISTENT, FileError.EMPTY))
            return;
        FileReader reader = configFile.getFileReader(base, ":");
        if(reader == null)
            return;
        Utils.loadSettings(reader, this);
        reader.close();
    }

    public abstract void validate();

}
