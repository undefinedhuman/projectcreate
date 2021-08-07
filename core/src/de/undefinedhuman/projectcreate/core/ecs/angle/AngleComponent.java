package de.undefinedhuman.projectcreate.core.ecs.angle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

public class AngleComponent implements Component, NetworkSerializable {

    public float angle = 0;
    public boolean isTurned;

    public Vector2 mousePos = new Vector2(0, 0);

    public AngleComponent() {
        this(false);
    }

    public AngleComponent(boolean isTurned) {
        this.isTurned = isTurned;
    }

    @Override
    public void receive(LineSplitter splitter) {
        this.mousePos = splitter.getNextVector2();
        this.angle = splitter.getNextFloat();
        this.isTurned = splitter.getNextBoolean();
    }

    @Override
    public void send(LineWriter writer) {
        writer.writeVector2(mousePos);
        writer.writeFloat(angle);
        writer.writeBoolean(isTurned);
    }

}
