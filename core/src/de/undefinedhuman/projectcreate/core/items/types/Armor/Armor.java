package de.undefinedhuman.projectcreate.core.items.types.Armor;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.crafting.RecipeType;
import de.undefinedhuman.projectcreate.engine.settings.types.StringArraySetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class Armor extends Item {

    public FloatSetting
            armor = new FloatSetting("Armor", 0f);
    public StringArraySetting
            inVisibleSprites = new StringArraySetting("Invisible Sprites", new String[0]);

    public Armor() {
        super();
        settings.addSettings(armor, inVisibleSprites);
        this.recipeType = RecipeType.ARMOR;
    }

}
