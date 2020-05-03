package de.undefinedhuman.sandboxgame.engine.utils;

import com.badlogic.gdx.graphics.Color;

public class Variables {
    // Engine
    public static final float GAME_CAMERA_ZOOM = 3f;
    public static final float DELTA_MULTIPLIER = 1.0f;
    public static final String SEPARATOR = ";", FILE_SEPARATOR = "/";
    public static final String DEFAULT_LANGUAGE = "eu_DE";
    public static final boolean RENDER_HITBOXES = true;
    public static final Color HITBOX_COLOR = new Color(0.41568628f, 0.3529412f, 0.8039216f, 0.4f);

    // World
    public static final int CHUNK_SIZE = 20;
    public static final int BLOCK_SIZE = 16;
    public static final int HOUR_LENGTH = 1000;

    // Background
    public static final int BASE_BACKGROUND_WIDTH = 688;
    public static final int CLOUD_COUNT = 5;
    public static final int CLOUD_HEIGHT_OFFSET = 30;
    public static final float CLOUD_BASE_SPEED = 0.1f;

    public static final int BIRD_WIDTH = 19;
    public static final int BIRD_HEIGHT = 64;
    public static final int BIRD_SPEED = 64;
    public static final int BIRD_HEIGHT_OFFSET = 32;
    public static final float BIRD_ANIMATION_SPEED = 0.0875f;

    // Gui
    public static final int SLOT_SIZE = 24;
    public static final int ITEM_SIZE = 16;
    public static final int SLOT_SPACE = 2;
    public static final int SELECTED_AMOUNT = 2;
    public static final int HOTBAR_OFFSET = 6;

    // Editor
    public static final int TEXTURE_OFFSET_PLAYER = 64;
}
