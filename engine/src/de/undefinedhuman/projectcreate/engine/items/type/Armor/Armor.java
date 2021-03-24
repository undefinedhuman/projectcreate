package de.undefinedhuman.projectcreate.engine.items.type.Armor;

import de.undefinedhuman.projectcreate.engine.items.Item;
import de.undefinedhuman.projectcreate.engine.items.recipe.RecipeType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.types.StringArraySetting;

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
