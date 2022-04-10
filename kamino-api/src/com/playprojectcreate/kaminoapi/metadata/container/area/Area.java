package com.playprojectcreate.kaminoapi.metadata.container.area;

import com.badlogic.gdx.math.Vector2;

public class Area {

    private final Vector2 min = new Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private final Vector2 max = new Vector2(Integer.MIN_VALUE, Integer.MIN_VALUE);

    public void addPosition(Vector2 value) {
        if(value.x < min.x) min.x = value.x;
        if(value.y < min.y) min.y = value.y;
        if(value.x > max.x) max.x = value.x;
        if(value.y > max.y) max.y = value.y;
    }

}
