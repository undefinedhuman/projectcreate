package de.undefinedhuman.projectcreate.engine.file;

import com.badlogic.gdx.Files;

public class Paths {

    public static final String LOG_PATH = "log/";
    public static final String CONFIG_PATH = "config/";
    public static final String SCREENSHOT_PATH = "screenshots/";
    public static final String PLUGIN_PATH = "plugins/";

    public static final String ENTITY_PATH = "entity/";
    public static final String SOUND_PATH = "sound/";
    public static final String LANGUAGE_PATH = "language/";
    public static final String ITEM_PATH = "item/";
    public static final String GUI_PATH = "gui/";
    public static final String EDITOR_PATH = "editor/";

    private static volatile Paths instance;

    private final FsFile directory;

    private Paths(String directoryPath, Files.FileType directoryFileType) {
        this.directory = new FsFile(directoryPath, directoryFileType);
    }

    public FsFile getDirectory() {
        return directory;
    }

    public static Paths getInstance() {
        return getInstance(".projectcreate/", Files.FileType.External);
    }

    public static Paths getInstance(String directoryPath, Files.FileType directoryFileType) {
        if(instance != null)
            return instance;
        synchronized (Paths.class) {
            if (instance == null)
                instance = new Paths(directoryPath, directoryFileType);
        }
        return instance;
    }

}