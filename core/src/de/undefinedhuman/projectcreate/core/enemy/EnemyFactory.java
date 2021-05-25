package de.undefinedhuman.projectcreate.core.enemy;

import de.undefinedhuman.projectcreate.core.items.types.Armor.Armor;
import de.undefinedhuman.projectcreate.core.items.types.weapons.Weapon;

public abstract class EnemyFactory {
    public abstract int getEntityID();
    public abstract Weapon createWeapon();
    public abstract Armor createArmor();
}
