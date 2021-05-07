package de.undefinedhuman.projectcreate.engine.config;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

@SuppressWarnings("ALL")
public abstract class Config extends SettingsList implements Serializable {

    private String fileName;
    private FsFile configFile;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public void init() {
        configFile = new FsFile(Paths.CONFIG_PATH, fileName + ".config", Files.FileType.External);
    }

    @Override
    public void save() {
        if(FileError.checkFileForErrors("saving Config (" + fileName + ")", configFile, FileError.NULL))
            return;
        FileWriter writer = configFile.getFileWriter(true);
        if(writer == null)
            return;
        Tools.saveSettings(writer, this);
        writer.close();
    }

    @Override
    public void load() {
        if(FileError.checkFileForErrors("loading Config (" + fileName + ")", configFile, FileError.NULL, FileError.NON_EXISTENT, FileError.EMPTY))
            return;
        FileReader reader = configFile.getFileReader(true);
        if(reader == null)
            return;
        Tools.loadSettings(reader, this);
        reader.close();
    }

    void validate() {}

}
