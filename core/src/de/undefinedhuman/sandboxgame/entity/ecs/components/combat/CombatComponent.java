package de.undefinedhuman.sandboxgame.entity.ecs.components.combat;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.ArrayList;

public class CombatComponent extends Component {

    public float currentDamage;
    public ArrayList<Entity> touchedEntityList = new ArrayList<>();
    public boolean canAttack = false, charged = false;

    public CombatComponent(Entity entity) {

        super(entity);
        this.type = ComponentType.COMBAT;

    }

    @Override
    public void setNetworkData(LineSplitter s) {}

    @Override
    public void getNetworkData(LineWriter w) {

    }

}
