package de.undefinedhuman.sandboxgame.engine.file;

public enum Paths {

    GAME_PATH("../sandboxgame/"), CONFIG_PATH(GAME_PATH.getPath() + "config/"), LOG_PATH(GAME_PATH.getPath() + "log/"), SCREENSHOT_PATH(GAME_PATH.getPath() + "screenshot/"),
    ENTITY_FOLDER("entity/"), SOUND_FOLDER("sounds/"), ITEM_PATH("items/"), GUI_PATH("gui/"), LANGUAGE_PATH("language/");

    private String path;

    Paths(String path) { this.path = path; }
    public String getPath() { return path; }

}
