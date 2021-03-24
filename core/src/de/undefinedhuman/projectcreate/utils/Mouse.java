package de.undefinedhuman.projectcreate.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Mouse {

    public static boolean isLeftClicked() {
        return Gdx.input.isButtonPressed(0);
    }

    public static boolean isRightClicked() {
        return Gdx.input.isButtonPressed(1);
    }

    public static Vector2 getMouseCoords() {
        return new Vector2(getX(), getY());
    }

    public static float getX() {
        return Gdx.input.getX();
    }

    public static float getY() {
        return Gdx.input.getY();
    }

}
