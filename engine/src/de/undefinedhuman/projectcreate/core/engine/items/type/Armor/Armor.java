package de.undefinedhuman.projectcreate.core.engine.items.type.Armor;

import de.undefinedhuman.projectcreate.core.engine.items.Item;
import de.undefinedhuman.projectcreate.core.engine.items.recipe.RecipeType;
import de.undefinedhuman.projectcreate.core.engine.settings.types.StringArraySetting;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

public class Armor extends Item {

    public Setting
            armor = new Setting(SettingType.Float, "Armor", 0f),
            inVisibleSprites = new StringArraySetting("Invisible Sprites", new String[0]);

    public Armor() {
        super();
        settings.addSettings(armor, inVisibleSprites);
        this.recipeType = RecipeType.ARMOR;
    }

}
