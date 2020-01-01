package de.undefinedhuman.sandboxgameserver.entity.ecs.components.mouse;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class AngleComponent extends Component {

    public Vector2 mousePos = new Vector2();
    public float angle = 0;
    public boolean isTurned = false;

    public AngleComponent(Entity entity) {

        super(entity);
        this.type = ComponentType.ANGLE;

    }

    @Override
    public void load(FileReader reader) {

        this.isTurned = reader.getNextBoolean();

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeBoolean(isTurned);

    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeVector2(mousePos);
        w.writeFloat(angle);
        w.writeBoolean(isTurned);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        this.mousePos = s.getNextVector2();
        this.angle = s.getNextFloat();
        this.isTurned = s.getNextBoolean();
    }

}
