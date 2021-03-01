package de.undefinedhuman.sandboxgame.engine.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Variables {
    // General
    public static final String NAME = "SandboxGame";
    public static final Version VERSION = new Version(0, 1, 0);

    // Engine
    public static final int LOG_LEVEL = Application.LOG_DEBUG;
    public static final float GAME_CAMERA_ZOOM = 3f;
    public static final float DELTA_MULTIPLIER = 1.0f;

    // File System
    public static final String SEPARATOR = ";";
    public static final String FILE_SEPARATOR = "/";

    // Language
    public static final String DEFAULT_LANGUAGE = "eu_DE";

    // Log
    public static final String LOG_MESSAGE_FORMAT = "[%prefix% - %time%] %message%";
    public static final String LOG_DATE_FORMAT = "dd-MM-yyyy - HH-mm-ss";

    public static final boolean DEBUG = false;
    public static final Color HITBOX_COLOR = new Color(0.41568628f, 0.3529412f, 0.8039216f, 0.4f);

    // World
    public static final int CHUNK_SIZE = 20;
    public static final int BLOCK_SIZE = 16;
    public static final int COLLISION_SIZE = 8;
    public static final int COLLISION_HITBOX_OFFSET = 4;
    public static final int HOUR_LENGTH = 1000;

    // Background
    public static final int BASE_BACKGROUND_WIDTH = 688;
    public static final int CLOUD_COUNT = 5;
    public static final int CLOUD_HEIGHT_OFFSET = 30;

    public static final Vector2 BIRD_SIZE = new Vector2(19, 39);
    public static final int BIRD_HEIGHT_OFFSET = 32;
    public static final int BIRD_SPEED = 64;
    public static final float BIRD_ANIMATION_SPEED = 0.0875f;

    // User interface
    public static final int BASE_WINDOW_WIDTH = 1280;
    public static final int BASE_WINDOW_HEIGHT = 720;
    public static final int SLOT_SIZE = 22;
    public static final int ITEM_SIZE = 16;
    public static final int SLOT_SPACE = 2;
    public static final int SELECTED_SIZE = 2;
    public static final int MOUSE_SENSITIVITY = 10;

    // Editor
    public static final int PLAYER_TEXTURE_OFFSET = 64;

    // Player
    public static final int BLOCK_PLACEMENT_RANGE = 6;
}
