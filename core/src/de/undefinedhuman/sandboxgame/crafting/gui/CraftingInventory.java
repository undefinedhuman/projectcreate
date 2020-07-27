package de.undefinedhuman.sandboxgame.crafting.gui;

import de.undefinedhuman.sandboxgame.crafting.Recipe;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.engine.utils.MultiMap;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class CraftingInventory extends Gui {

    public static CraftingInventory instance;

    private Gui recipeBackground, menuBackground;

    private MultiMap<RecipeType, Recipe> recipes = new MultiMap<>();
    private int currentRecipeIndex = 0;
    private RecipeType currentRecipeType = null;

    private Gui[] recipeGuis = new Gui[8];

    public CraftingInventory() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null) instance = this;
        set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 15)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10)));
        setOffset(new CenterOffset(), new CenterOffset());
        initBackgrounds();
        initRecipes();
        GuiManager.instance.addGui(this);
    }

    private void initBackgrounds() {
        addChild(
                menuBackground = (Gui) new Gui(GuiTemplate.HOTBAR).set(new PixelConstraint(getBaseCornerSize()), new PixelConstraint(calculateConstraintValue(Axis.HEIGHT) - getBaseCornerSize()), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.HOTBAR, 1))).setOffsetY(new RelativeOffset(-1)),
                recipeBackground = (Gui) new Gui(GuiTemplate.HOTBAR).set(new PixelConstraint(getBaseCornerSize()), new PixelConstraint(getBaseCornerSize()), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.HOTBAR, 8)))
        );
    }

    private void initRecipes() {
        for(int i = 0; i < recipeGuis.length; i++) {
            recipeGuis[i] = (Gui) new Gui(GuiTemplate.SLOT)
                    .set(new RelativeConstraint(0.5f), new PixelConstraint(GuiTemplate.HOTBAR.cornerSize + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), new PixelConstraint(recipeBackground.getBaseValue(Axis.WIDTH) - GuiTemplate.HOTBAR.cornerSize * 2), new PixelConstraint(Variables.SLOT_SIZE)).setOffsetX(new CenterOffset());
            recipeBackground.addChild(recipeGuis[i]);
        }
    }

    public void addRecipes(Recipe... recipes) {
        clear();

        for(Recipe recipe : recipes)
            this.recipes.addValuesWithKey(recipe.getRecipeType(), recipe);

        RecipeType[] recipeTypes = (RecipeType[]) this.recipes.keySet().toArray();
        currentRecipeType = recipeTypes[0];

        addRecipeTypesToMenu(recipeTypes);

    }

    private void addRecipeTypesToMenu(RecipeType... recipeTypes) {
        for(int i = 0; i < recipeTypes.length; i++) {
            final RecipeType type = recipeTypes[i];
            this.menuBackground.addChild(new MenuSlot(type.getPreviewTexture(), new PixelConstraint(i * GuiTemplate.HOTBAR.cornerSize + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), new RelativeConstraint(0.5f)) {
                @Override
                public void onClick() {
                    updateArticles(type);
                }
            });
        }
    }

    public void updateArticles(RecipeType type) {

    }

    public void clear() {
        currentRecipeIndex = 0;
        recipeBackground.clearChildren();
        menuBackground.clearChildren();
    }

    /*private void addMenu(Gui background, ItemType... types) {
        for (int i = 0; i < types.length; i++)
            background.addChild(new MenuSlot(types[i].getPreviewTexture(), (Variables.HOTBAR_OFFSET + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), (int) (background.getBaseValue(Axis.HEIGHT) - Variables.HOTBAR_OFFSET - Variables.SLOT_SIZE)) {
                @Override
                public void onClick() {}
            });
    }*/

    /*private void addRecipes(Gui background) {
        for (int i = 0; i < 8; i++) {
            background.addChild(new Gui(GuiTemplate.SLOT)
                    .set("r0.5", "p" + (Variables.HOTBAR_OFFSET + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), "p" + (background.getBaseValue(Axis.WIDTH) - Variables.HOTBAR_OFFSET * 2), "p" + Variables.SLOT_SIZE)
                    .setCenteredX());
        }
        background.addChild(new Gui("gui/arrowdown.png").set("r0.5", "p0", "p25", "p25").setCentered());
    }*/

}
