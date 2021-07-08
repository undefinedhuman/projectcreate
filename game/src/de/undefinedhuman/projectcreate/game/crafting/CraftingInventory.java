package de.undefinedhuman.projectcreate.game.crafting;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.core.crafting.RecipeType;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;
import de.undefinedhuman.projectcreate.game.crafting.recipe.RecipeGui;
import de.undefinedhuman.projectcreate.game.crafting.recipe.RecipePreviewPanel;
import de.undefinedhuman.projectcreate.game.gui.Gui;
import de.undefinedhuman.projectcreate.game.gui.elements.scrollpanel.PooledScrollPanel;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.game.inventory.slot.MenuSlot;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class CraftingInventory extends Gui {

    private static volatile CraftingInventory instance;

    private Gui menuBackground;
    private PooledScrollPanel<RecipeGui> recipesScrollPanel;

    private RecipePreviewPanel recipePreviewPanel;

    private MultiMap<RecipeType, Integer> recipes = new MultiMap<>();
    private RecipeType currentRecipeType = null;

    private CraftingInventory() {
        super(GuiTemplate.SMALL_PANEL);
        setPosition(new CenterConstraint(), new CenterConstraint());
        setSize(Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 15), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 10));
        setOffset(new CenterOffset(), new CenterOffset());
        initBackgrounds();
        setTitle("Crafting", Font.Title, Color.WHITE);
    }

    @Override
    public void init() {
        super.init();
        setRecipes(1, 2, 3);
    }

    private void initBackgrounds() {
        recipesScrollPanel = new PooledScrollPanel<>(GuiTemplate.HOTBAR, RecipeGui::new);
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
            this.recipes.add(ItemManager.getInstance().getItem(itemID).recipeType, itemID);

        ArrayList<RecipeType> recipeTypes = new ArrayList<>(this.recipes.keySet());
        addRecipeTypesToMenu(recipeTypes);
        if(menuBackground.getChildren().size() <= 0)
            return;
        ((MenuSlot) menuBackground.getChildren().get(0)).listener.onClick();
    }

    private void addRecipeTypesToMenu(ArrayList<RecipeType> recipeTypes) {
        int i = 0;
        for(RecipeType recipeType : RecipeType.values()) {
            if(!recipeTypes.contains(recipeType))
                continue;
            this.menuBackground.addChild(new MenuSlot(new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i++), new RelativeConstraint(0.5f), recipeType.getPreviewTexture(), () -> {
                if(currentRecipeType == recipeType)
                    return;

                currentRecipeType = recipeType;
                updateRecipes();
            }).setOffsetY(new CenterOffset()));
        }
    }

    public void updateRecipes() {
        recipesScrollPanel.clear();
        List<Integer> recipeIDs = recipes.getValuesWithKey(currentRecipeType);
        for(int i = recipeIDs.size()-1; i >= 0; i--)
            recipesScrollPanel.addContent().update(recipeIDs.get(i));
    }

    public void updateRecipe(int itemID) {
        recipePreviewPanel.update(itemID);
    }

    public void clear() {
        menuBackground.deleteChildren();
        recipesScrollPanel.clear();
    }

    public static CraftingInventory getInstance() {
        if (instance == null) {
            synchronized (CraftingInventory.class) {
                if (instance == null)
                    instance = new CraftingInventory();
            }
        }
        return instance;
    }

}
