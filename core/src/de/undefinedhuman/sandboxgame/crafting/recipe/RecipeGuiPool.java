package de.undefinedhuman.sandboxgame.crafting.recipe;

import de.undefinedhuman.sandboxgame.engine.utils.ObjectPool;

public abstract class RecipeGuiPool extends ObjectPool<RecipeGui> {

    public RecipeGuiPool(long timeUntilDeletion) {
        super(timeUntilDeletion);
    }

    @Override
    protected RecipeGui createInstance() {
        return new RecipeGui() {
            @Override
            public void onClick(int itemID) {
                RecipeGuiPool.this.onClick(itemID);
            }
        };
    }

    @Override
    public boolean validate(RecipeGui object) {
        return object.getChildren().size() == RecipeGui.CHILD_AMOUNT;
    }

    @Override
    public void delete(RecipeGui object) {
        object.delete();
    }

    public abstract void onClick(int itemID);

}
