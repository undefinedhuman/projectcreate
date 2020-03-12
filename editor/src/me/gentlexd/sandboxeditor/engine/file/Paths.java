package me.gentlexd.sandboxeditor.engine.file;

public enum Paths {

    GAME_PATH("sandboxgame/"), LOG_PATH(GAME_PATH.getPath() + "log/"),
    RES_FOLDER("core/assets/"), ENTITY_FOLDER(RES_FOLDER.getPath() + "entitys/"), SOUND_FOLDER("sounds/"), BLOCK_PATH(RES_FOLDER.getPath() + "blocks/"), ITEM_PATH(RES_FOLDER.getPath() + "items/"), BLOCK2_PATH("blocks/");

    private String path;

    Paths(String path) {

        this.path = path;

    }

    public String getPath() {

        return path;

    }

}
