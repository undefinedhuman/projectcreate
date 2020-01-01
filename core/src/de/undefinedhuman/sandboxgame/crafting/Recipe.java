package de.undefinedhuman.sandboxgame.crafting;

public class Recipe {

    private RecipeItem[] resources;
    private RecipeItem result;

    public Recipe(RecipeItem[] resources, RecipeItem result) {
        this.resources = resources;
        this.result = result;
    }

}
