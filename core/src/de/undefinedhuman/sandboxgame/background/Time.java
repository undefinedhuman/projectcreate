package de.undefinedhuman.sandboxgame.background;

import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public enum Time {

    DAY("background/layer/Background.png", Variables.HOUR_LENGTH),
    SUNRISE("background/Sunrise.png", Variables.HOUR_LENGTH),
    MIDDAY("background/Day.png", Variables.HOUR_LENGTH * 4),
    SUNSET("background/Evening.png", Variables.HOUR_LENGTH),
    EVENING("background/Night.png", Variables.HOUR_LENGTH),
    MIDNIGHT("background/Midnight.png", Variables.HOUR_LENGTH * 4),
    MORNING("background/Night.png", Variables.HOUR_LENGTH);

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
        TextureManager.instance.addTexture(Time.getTextures());
    }

    public static String[] getTextures() {
        String[] textures = new String[values().length];
        for (int i = 0; i < values().length; i++) textures[i] = values()[i].texture;
        return textures;
    }

    public static void delete() {
        TextureManager.instance.removeTexture(Time.getTextures());
    }

}
