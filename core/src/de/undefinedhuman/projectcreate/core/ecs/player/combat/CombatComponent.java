package de.undefinedhuman.projectcreate.core.ecs.player.combat;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.base.GameObject;

import java.util.ArrayList;

public class CombatComponent implements Component {

    public float currentDamage;
    public ArrayList<GameObject> touchedEntityList = new ArrayList<>();
    public boolean canAttack = false, charged = false;

}
