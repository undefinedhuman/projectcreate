package de.undefinedhuman.sandboxgameserver.entity.ecs.components.combat;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

import java.util.ArrayList;

public class CombatComponent extends Component {

    public float currentDamage;
    public ArrayList<Entity> touchedEntityList = new ArrayList<>();
    public boolean canAttack = false;

    public CombatComponent(Entity entity) {

        super(entity);
        this.type = ComponentType.COMBAT;

    }

    @Override
    public void load(FileReader reader) { }

    @Override
    public void save(FileWriter writer) { }

    @Override
    public void getNetworkData(LineWriter w) { }

    @Override
    public void setNetworkData(LineSplitter s) { }

}
