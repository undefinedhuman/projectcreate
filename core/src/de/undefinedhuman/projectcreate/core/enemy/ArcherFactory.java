package de.undefinedhuman.projectcreate.core.enemy;

import de.undefinedhuman.projectcreate.core.items.Armor.Armor;
import de.undefinedhuman.projectcreate.core.items.Armor.Helmet;
import de.undefinedhuman.projectcreate.core.items.weapons.Bow;
import de.undefinedhuman.projectcreate.core.items.weapons.Weapon;

public class ArcherFactory extends EnemyFactory {
    @Override
    public int getEntityID() {
        return 6;
    }

    @Override
    public Weapon createWeapon() {
        Bow bow = new Bow();
        bow.range.setValue(200);
        bow.strength.setValue(20);
        return bow;
    }

    @Override
    public Armor createArmor() {
        Helmet helmet = new Helmet();
        helmet.armor.setValue(15f);
        return helmet;
    }
}
