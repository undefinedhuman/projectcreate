package de.undefinedhuman.projectcreate.engine.utils;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.log.Level;

import java.awt.*;
import java.io.File;

public class Variables {
    // General
    public static String NAME = "ProjectCreate";

    // Engine
    public static final float GAME_CAMERA_ZOOM = 3f;
    public static final String DEFAULT_TEXTURE = "Unknown.png";

    // Settings
    public static final int DEFAULT_CONTENT_WIDTH = 400;
    public static final int DEFAULT_CONTENT_HEIGHT = 25;
    public static final int OFFSET = 5;

    // File System
    public static final String SEPARATOR = ";";
    public static final String FILE_SEPARATOR = File.separator;

    // Language
    public static final String DEFAULT_LANGUAGE = "eu_DE";

    // Log
    public static final int LOG_DELETION_TIME_DAYS = 14;
    public static Level LOG_LEVEL = Level.INFO;
    public static final String LOG_DATE_FORMAT = "dd-MM-yyyy - HH-mm-ss";

    public static final boolean DEBUG = false;

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
    public static final int PLAYER_TEXTURE_BASE_HEIGHT = 32;
    public static final int PLAYER_TEXTURE_BASE_WIDTH = 64;
    public static final Color BACKGROUND_COLOR = new Color(60, 63, 65);

    // Server & Editor
    public static boolean DONT_LOAD_TEXTURES = false;

    // Server
    public static final int SERVER_TICK_RATE = 20;

    // Player
    public static final int BLOCK_PLACEMENT_RANGE = 6;

}
