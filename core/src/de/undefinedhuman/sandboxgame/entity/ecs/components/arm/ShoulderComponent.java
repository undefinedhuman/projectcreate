package de.undefinedhuman.sandboxgame.entity.ecs.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class ShoulderComponent extends Component {

    private Vector2[] shoulderPos, shoulderOffsets;

    public ShoulderComponent(Entity entity, Vector2[] shoulderPos, Vector2[] shoulderOffsets) {

        super(entity);
        this.shoulderPos = shoulderPos;
        this.shoulderOffsets = shoulderOffsets;
        this.type = ComponentType.SHOULDER;

    }

    public Vector2 getShoulderPos(int index) {
        return shoulderPos[index];
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

    @Override
    public void setNetworkData(LineSplitter s) {}

    @Override
    public void getNetworkData(LineWriter w) {}

}
