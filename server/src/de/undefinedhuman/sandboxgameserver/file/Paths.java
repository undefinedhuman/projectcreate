package de.undefinedhuman.sandboxgameserver.file;

public enum Paths {

    GAME_PATH("../../sandboxgameserver/"), LOG_PATH(GAME_PATH.getPath() + "log/"), USER_PATH(GAME_PATH.getPath() + "users/"), PLAYER_PATH(GAME_PATH.getPath() + "players/"), WORLD_PATH(GAME_PATH.getPath() + "worlds/"),
    ENTITY_FOLDER("../assets/entity/"), ITEM_PATH("items/");

    private String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
