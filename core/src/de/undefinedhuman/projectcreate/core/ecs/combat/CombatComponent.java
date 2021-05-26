package de.undefinedhuman.projectcreate.core.ecs.combat;

import de.undefinedhuman.projectcreate.engine.base.GameObject;
import de.undefinedhuman.projectcreate.engine.ecs.Component;

import java.util.ArrayList;

public class CombatComponent extends Component {

    public float currentDamage;
    public ArrayList<GameObject> touchedEntityList = new ArrayList<>();
    public boolean canAttack = false, charged = false;

}
