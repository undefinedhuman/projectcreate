package de.undefinedhuman.sandboxgame.engine.items.type.Armor;

import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.StringArraySetting;

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
