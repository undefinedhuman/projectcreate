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

    public Vector2 mousePos = new Vector2(0,0);

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
    public void setNetworkData(LineSplitter s) {
        this.mousePos = s.getNextVector2();
        this.angle = s.getNextFloat();
        this.isTurned = s.getNextBoolean();
    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeVector2(mousePos);
        w.writeFloat(angle);
        w.writeBoolean(isTurned);
    }

}
