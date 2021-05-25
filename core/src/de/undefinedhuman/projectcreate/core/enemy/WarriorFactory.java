package de.undefinedhuman.projectcreate.core.enemy;

import de.undefinedhuman.projectcreate.core.items.types.Armor.Armor;
import de.undefinedhuman.projectcreate.core.items.types.Armor.Helmet;
import de.undefinedhuman.projectcreate.core.items.types.weapons.Sword;
import de.undefinedhuman.projectcreate.core.items.types.weapons.Weapon;

public class WarriorFactory extends EnemyFactory {
    @Override
    public int getEntityID() {
        return 5;
    }

    @Override
    public Weapon createWeapon() {
        Sword sword = new Sword();
        sword.damage.setValue(10f);
        return sword;
    }

    @Override
    public Armor createArmor() {
        Helmet helmet = new Helmet();
        helmet.armor.setValue(40f);
        return null;
    }
}
