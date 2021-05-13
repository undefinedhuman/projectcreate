package de.undefinedhuman.projectcreate.core.ecs.combat;

import de.undefinedhuman.projectcreate.engine.base.GameObject;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

import java.util.ArrayList;

public class CombatComponent extends Component {

    public float currentDamage;
    public ArrayList<GameObject> touchedEntityList = new ArrayList<>();
    public boolean canAttack = false, charged = false;

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

}
