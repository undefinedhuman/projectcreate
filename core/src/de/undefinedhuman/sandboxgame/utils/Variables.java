package de.undefinedhuman.sandboxgame.utils;

import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;

public class Variables {

    // Engine
    public static final float deltaMultiplier = 1.0f;
    public static final String SEPARATOR = ";", FILE_SEPARATOR = "/";
    public static final String DEFAULT_LANGUAGE = "eu_DE";
    public static final String LANGUAGES_FILE = "language/languages.xml";
    public static final boolean renderHitboxes = false;

    // World
    public static final int CHUNK_SIZE = 20;
    public static final int BLOCK_SIZE = 16;
    public static final int HOUR_LENGTH = 10;

    // Gui
    public static final int SLOT_SIZE = 24;
    public static final int ITEM_SIZE = 16;
    public static final int SLOT_SPACE = 2;
    public static final int SELECTED_AMOUNT = 2;
    public static final int HOTBAR_OFFSET = 6;

    public static String getInventoryWidth(GuiTemplate template, int col) {
        return getInventoryWidth(template.cornerSize, col);
    }

    public static String getInventoryWidth(float offsetX, int col) {
        return "p" + (offsetX * 2 + (SLOT_SIZE * col + SLOT_SPACE * (col - 1)));
    }

    public static String getInventoryHeight(GuiTemplate template, int row) {
        return getInventoryHeight(template.cornerSize, row);
    }

    public static String getInventoryHeight(float offsetY, int row) {
        return "p" + (offsetY * 2 + (SLOT_SIZE * row + SLOT_SPACE * (row - 1)));
    }

}
