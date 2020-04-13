package de.undefinedhuman.sandboxgame.engine.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public class ShoulderComponent extends Component {

    private Vector2[] shoulderPos, shoulderOffsets;

    public ShoulderComponent(Vector2[] shoulderPos, Vector2[] shoulderOffsets) {
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
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

}
