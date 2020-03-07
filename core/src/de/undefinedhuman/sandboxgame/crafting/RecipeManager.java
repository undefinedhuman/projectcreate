package de.undefinedhuman.sandboxgame.crafting;

import java.util.HashMap;

public class RecipeManager {

    public static RecipeManager instance;
    public HashMap<Integer, Recipe> recipes;

    public RecipeManager() {
        this.recipes = new HashMap<>();
    }

    public boolean hasRecipe(int id) {
        return recipes.containsKey(id);
    }

    public Recipe getRecipe(int id) {
        return recipes.get(id);
    }

}
