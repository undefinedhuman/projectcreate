package de.undefinedhuman.projectcreate.core.engine.items.type.tools;

import de.undefinedhuman.projectcreate.core.engine.items.Item;
import de.undefinedhuman.projectcreate.core.engine.items.recipe.RecipeType;

public class Tool extends Item {

    public Tool() {
        super();
        this.recipeType = RecipeType.TOOL;
    }

}
