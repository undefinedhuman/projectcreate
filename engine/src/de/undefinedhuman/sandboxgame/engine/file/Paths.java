package de.undefinedhuman.sandboxgame.engine.file;

import de.undefinedhuman.sandboxgame.engine.Engine;

public enum Paths {

    GAME_PATH("../../sandboxgame/"), SERVER_PATH("../../sandboxgame-server/"), CONFIG_PATH(Engine.instance.externalPath + "config/"), LOG_PATH(Engine.instance.externalPath + "log/"), SCREENSHOT_PATH(Engine.instance.externalPath + "screenshot/"),
    GAME_ASSET_PATH(""), SERVER_ASSET_PATH("assets/"), ENTITY_FOLDER(Engine.instance.assetPath + "entity/"), SOUND_FOLDER(Engine.instance.assetPath + "sounds/"), ITEM_PATH(Engine.instance.assetPath + "items/"), GUI_PATH(Engine.instance.assetPath + "gui/");

    private String path;

    Paths(String path) { this.path = path; }
    public String getPath() { return path; }

}
