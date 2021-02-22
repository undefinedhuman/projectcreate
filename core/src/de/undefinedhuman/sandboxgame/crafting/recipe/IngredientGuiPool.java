package de.undefinedhuman.sandboxgame.crafting.recipe;

import de.undefinedhuman.sandboxgame.engine.utils.ds.ObjectPool;

public class IngredientGuiPool extends ObjectPool<IngredientGui> {

    public IngredientGuiPool(long timeUntilDeletion) {
        super(timeUntilDeletion);
    }

    @Override
    protected IngredientGui createInstance() {
        return new IngredientGui();
    }

    @Override
    public boolean validate(IngredientGui object) {
        return object.getChildren().size() == IngredientGui.CHILD_AMOUNT;
    }

    @Override
    public void delete(IngredientGui object) {
        object.delete();
    }

}
