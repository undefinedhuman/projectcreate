package de.undefinedhuman.sandboxgame.engine.items.type.tools;

import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;

public class Tool extends Item {

    public Tool() {
        super();
        this.recipeType = RecipeType.TOOL;
    }

}
