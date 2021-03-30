package de.undefinedhuman.projectcreate.core.engine.entity.components.mouse;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.core.engine.file.LineWriter;

public class AngleComponent extends Component {

    public float angle = 0;
    public boolean isTurned;

    public Vector2 mousePos = new Vector2(0, 0);

    public AngleComponent() {
        this(false);
    }

    public AngleComponent(boolean isTurned) {
        this.isTurned = isTurned;
        this.type = ComponentType.ANGLE;
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
