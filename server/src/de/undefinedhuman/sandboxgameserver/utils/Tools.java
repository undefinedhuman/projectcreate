package de.undefinedhuman.sandboxgameserver.utils;

public class Tools {

    public static float lerp(float a, float b, float f) {
        return a * (1 - f) + (b * f);
    }

}
