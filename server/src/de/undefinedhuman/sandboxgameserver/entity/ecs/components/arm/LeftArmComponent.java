package de.undefinedhuman.sandboxgameserver.entity.ecs.components.arm;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class LeftArmComponent extends Component {

    public float shakeAngle = 60;
    public boolean shakeDirection = false;

    public LeftArmComponent(Entity entity) {
        super(entity);
        this.type = ComponentType.LEFTARM;
    }

    @Override
    public void load(FileReader reader) { }

    @Override
    public void save(FileWriter writer) { }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeFloat(shakeAngle);
        w.writeBoolean(shakeDirection);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        this.shakeAngle = s.getNextFloat();
        this.shakeDirection = s.getNextBoolean();
    }

}
