package de.undefinedhuman.projectcreate.engine.utils;

import com.badlogic.gdx.graphics.Color;

public class Colors {
    private Colors() {
        throw new UnsupportedOperationException("Container class to centralize main colors for the game");
    }

    public static final Color HITBOX_COLOR = new Color(0.41568628f, 0.3529412f, 0.8039216f, 0.4f);

    public static final Color WHITE = new Color(0.8f, 0.8f, 0.8f, 1f);
    public static final Color LIGHT_GREEN = new Color(0.22f, 0.66f, 0.2f, 1f);
    public static final Color LIGHT_BLUE = new Color(0.21f, 0.54f, 0.63f, 1f);
    public static final Color PURPLE = new Color(0.44f, 0.21f, 0.63f, 1f);
    public static final Color ORANGE = new Color(0.64f, 0.59f, 0.2f, 1f);
    public static final Color RED = new Color(0.85f, 0.22f, 0.22f, 1f);
}
