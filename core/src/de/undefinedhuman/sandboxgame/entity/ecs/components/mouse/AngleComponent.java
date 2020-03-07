package de.undefinedhuman.sandboxgame.entity.ecs.components.mouse;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class AngleComponent extends Component {

    public float angle = 0;
    public boolean isTurned = false;

    public Vector2 mousePos = new Vector2(0, 0);

    public AngleComponent(Entity entity) {
        super(entity);
        this.type = ComponentType.ANGLE;
    }

    public AngleComponent(Entity entity, boolean isTurned) {
        super(entity);
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
