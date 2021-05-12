package de.undefinedhuman.projectcreate.game.background;

import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public enum Time {

    DAY("background/layer/Background.png", Variables.HOUR_LENGTH),
    SUNRISE("background/layer/Background.png", Variables.HOUR_LENGTH),
    MIDDAY("background/layer/Background.png", Variables.HOUR_LENGTH * 4),
    SUNSET("background/layer/Background.png", Variables.HOUR_LENGTH),
    EVENING("background/layer/Background.png", Variables.HOUR_LENGTH),
    MIDNIGHT("background/layer/Background.png", Variables.HOUR_LENGTH * 4),
    MORNING("background/layer/Background.png", Variables.HOUR_LENGTH);

    public String texture;
    public int duration;

    Time(String texture, int duration) {
        this.texture = texture;
        this.duration = duration;
    }

    public static Time valueOf(int id) {
        return values()[id];
    }

    public static void load() {
        TextureManager.instance.loadTextures(Time.getTextures());
    }

    public static String[] getTextures() {
        String[] textures = new String[values().length];
        for (int i = 0; i < values().length; i++) textures[i] = values()[i].texture;
        return textures;
    }

    public static void delete() {
        TextureManager.instance.removeTextures(Time.getTextures());
    }

}
