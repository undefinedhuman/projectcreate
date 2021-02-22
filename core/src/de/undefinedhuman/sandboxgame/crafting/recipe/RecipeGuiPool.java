package de.undefinedhuman.sandboxgame.crafting.recipe;

import de.undefinedhuman.sandboxgame.engine.utils.ds.ObjectPool;

public class RecipeGuiPool extends ObjectPool<RecipeGui> {

    public RecipeGuiPool(long timeUntilDeletion) {
        super(timeUntilDeletion);
    }

    @Override
    protected RecipeGui createInstance() {
        return new RecipeGui();
    }

    @Override
    public boolean validate(RecipeGui object) {
        return object.getChildren().size() == RecipeGui.CHILD_AMOUNT;
    }

    @Override
    public void delete(RecipeGui object) {
        object.delete();
    }

}
