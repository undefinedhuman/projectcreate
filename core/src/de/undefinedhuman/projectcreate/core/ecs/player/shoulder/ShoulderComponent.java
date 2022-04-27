package de.undefinedhuman.projectcreate.core.ecs.player.shoulder;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class ShoulderComponent implements Component {

    private Vector2[] shoulderPos;

    public ShoulderComponent(Vector2[] shoulderPos, Vector2[] shoulderOffsets) {
        this.shoulderPos = shoulderPos;
    }

    public Vector2 getShoulderPos(int index) {
        if(!Utils.isInRange(index, 0, shoulderPos.length-1))
            return new Vector2();
        return shoulderPos[index];
    }

}
