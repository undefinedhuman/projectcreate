package de.undefinedhuman.sandboxgame.crafting;

import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.item.ItemManager;

public class Recipe {

    public RecipeItem[] resources;
    public RecipeItem result;

    public Recipe(RecipeItem[] resources, RecipeItem result) {
        this.resources = resources;
        this.result = result;
    }

    public RecipeType getRecipeType() {
        return ItemManager.instance.getItem(result.id).recipeType;
    }

}
