package de.undefinedhuman.sandboxgame.utils;

import com.badlogic.gdx.math.Vector2;

public class Variables {

    // Engine
    public static float deltaMultiplier = 1.0f;
    public static String SEPARATOR = ";", FILE_SEPARATOR = "/";
    public static String DEFAULT_LANGUAGE = "eu_DE";
    public static String LANGUAGES_FILE = "language/languages.xml";
    public static boolean renderHitboxes = false;

    // World
    public static int CHUNK_SIZE = 20;
    public static int BLOCK_SIZE = 16;
    public static int HOUR_LENGTH = 10;

    // Gui
    public static int SLOT_SIZE = 24, SLOT_SPACE = 2, SELECTED_AMOUNT = 2, ITEM_SIZE = 16;

    public static Vector2 getInventoryScale(Vector2 offset, int col, int row) {
        return new Vector2(SLOT_SIZE * col + SLOT_SPACE * (col-1) + offset.x * 2,SLOT_SIZE * row + SLOT_SPACE * (row-1) + offset.y * 2);
    }

}
