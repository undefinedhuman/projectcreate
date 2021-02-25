package de.undefinedhuman.sandboxgame.crafting;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.crafting.recipe.RecipeGui;
import de.undefinedhuman.sandboxgame.crafting.recipe.RecipeGuiPool;
import de.undefinedhuman.sandboxgame.crafting.recipe.RecipePreviewPanel;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.utils.ds.MultiMap;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.elements.scrollpanel.ScrollPanel;
import de.undefinedhuman.sandboxgame.gui.pool.GuiPool;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;

public class CraftingInventory extends Gui {

    public static CraftingInventory instance;

    private Gui menuBackground;
    private ScrollPanel<RecipeGui> recipesScrollPanel;
    private RecipePreviewPanel recipePreviewPanel;

    private MultiMap<RecipeType, Integer> recipes = new MultiMap<>();
    private RecipeType currentRecipeType = null;

    public CraftingInventory() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null)
            instance = this;
        setPosition(new CenterConstraint(), new CenterConstraint());
        setSize(Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 15), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 10));
        setOffset(new CenterOffset(), new CenterOffset());
        initBackgrounds();
        setTitle("Crafting", Font.Title, Color.WHITE);
    }

    @Override
    public void init() {
        super.init();
        setRecipes(2);
    }

    private void initBackgrounds() {
        recipesScrollPanel = new ScrollPanel<>(GuiTemplate.HOTBAR, RecipeGui.class);
        recipesScrollPanel.setSize(Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 5), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 8));

        addChild(
                menuBackground = (Gui) new Gui(GuiTemplate.HOTBAR)
                        .set(new PixelConstraint(0), new RelativeConstraint(1), new RelativeConstraint(1f), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 1))
                        .setOffsetY(new RelativeOffset(-1)),
                recipesScrollPanel,
                recipePreviewPanel = new RecipePreviewPanel()
        );
    }

    public void setRecipes(int... itemIDs) {
        clear();
        for(int itemID : itemIDs)
            this.recipes.add(ItemManager.instance.getItem(itemID).recipeType, itemID);

        ArrayList<RecipeType> recipeTypes = new ArrayList<>(this.recipes.keySet());
        addRecipeTypesToMenu(recipeTypes);
        if(menuBackground.getChildren().size() <= 0)
            return;
        ((MenuSlot) menuBackground.getChildren().get(0)).onClick();
    }

    private void addRecipeTypesToMenu(ArrayList<RecipeType> recipeTypes) {
        int i = 0;
        for(RecipeType recipeType : RecipeType.values()) {
            if(!recipeTypes.contains(recipeType))
                continue;
            this.menuBackground.addChild(new MenuSlot(recipeType.getPreviewTexture(), new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i++), new RelativeConstraint(0.5f)) {
                @Override
                public void onClick() {
                    if(currentRecipeType == recipeType)
                        return;

                    currentRecipeType = recipeType;
                    updateRecipes();
                }
            }.setOffsetY(new CenterOffset()));
        }
    }

    public void updateRecipes() {
        recipesScrollPanel.clear();
        for(int itemID : recipes.getValuesWithKey(currentRecipeType)) {
            recipesScrollPanel.addContent(recipeGuiPool.get().update(itemID));
        }
    }

    public void updateRecipe(int itemID) {
        recipePreviewPanel.update(itemID);
    }

    public void clear() {
        menuBackground.deleteChildren();
        recipesScrollPanel.clear();
    }

}
