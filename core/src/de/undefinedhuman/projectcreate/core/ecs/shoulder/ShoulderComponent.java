package de.undefinedhuman.projectcreate.core.ecs.shoulder;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class ShoulderComponent implements Component {

    private Vector2[] shoulderPos, shoulderOffsets;

    public ShoulderComponent(Vector2[] shoulderPos, Vector2[] shoulderOffsets) {
        this.shoulderPos = shoulderPos;
        this.shoulderOffsets = shoulderOffsets;
    }

    public Vector2 getShoulderPos(int index) {
        if(!Tools.isInRange(index, 0, shoulderPos.length-1))
            return new Vector2();
        Vector2 vector = shoulderPos[index];
        return new Vector2(vector);
    }

    public Vector2 getShoulderOffset(int index) {
        return shoulderOffsets[index];
    }

    public Vector2[] getShoulderPos() {
        return shoulderPos;
    }

    public Vector2[] getShoulderOffsets() {
        return shoulderOffsets;
    }

}
