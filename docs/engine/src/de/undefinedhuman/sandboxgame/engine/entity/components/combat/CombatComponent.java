package de.undefinedhuman.sandboxgame.engine.entity.components.combat;

import de.undefinedhuman.sandboxgame.engine.base.GameObject;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

import java.util.ArrayList;

public class CombatComponent extends Component {

    public float currentDamage;
    public ArrayList<GameObject> touchedEntityList = new ArrayList<>();
    public boolean canAttack = false, charged = false;

    public CombatComponent() {
        this.type = ComponentType.COMBAT;
    }

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

}
