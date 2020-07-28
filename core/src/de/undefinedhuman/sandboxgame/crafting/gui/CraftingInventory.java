package de.undefinedhuman.sandboxgame.crafting.gui;

import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.MultiMap;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
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

    private static final int VIEWABLE_RECIPES = 8;

    private Gui recipeBackground, menuBackground;

    private MultiMap<RecipeType, Integer> recipes = new MultiMap<>();
    private int currentRecipeIndex = 0;
    private RecipeType currentRecipeType = null;

    private Gui[] recipeGuis = new Gui[VIEWABLE_RECIPES];

    public CraftingInventory() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null) instance = this;
        set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 15)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10)));
        setOffset(new CenterOffset(), new CenterOffset());
        initBackgrounds();
        initRecipes();
        addRecipes(2);
        GuiManager.instance.addGui(this);
    }

    private void initBackgrounds() {
        addChild(
                menuBackground = (Gui) new Gui(GuiTemplate.HOTBAR).set(new PixelConstraint(0), new PixelConstraint(calculateConstraintValue(Axis.HEIGHT) - getBaseCornerSize() * 2), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.HOTBAR, 1))).setOffsetY(new RelativeOffset(-1)),
                recipeBackground = (Gui) new Gui(GuiTemplate.HOTBAR).setSize(new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.HOTBAR, 8)))
        );
    }

    private void initRecipes() {
        for(int i = 0; i < recipeGuis.length; i++)
            recipeGuis[i] = (Gui) new Gui(GuiTemplate.SLOT).set(new RelativeConstraint(0.5f), new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), new PixelConstraint(recipeBackground.getBaseValue(Axis.WIDTH) - GuiTemplate.HOTBAR.cornerSize * 2), new PixelConstraint(Variables.SLOT_SIZE)).setOffsetX(new CenterOffset());
        recipeBackground.addChild(recipeGuis);
        recipeBackground.addChild(
                new Gui("gui/arrowup.png").addEvent(new ClickEvent() {
                    @Override
                    public void onClick() {
                        scroll(-1);
                    }
                }).set(new CenterConstraint(), new RelativeConstraint(1), new PixelConstraint(32), new PixelConstraint(32)).setOffset(new CenterOffset(), new CenterOffset()),
                new Gui("gui/arrowdown.png").addEvent(new ClickEvent() {
                    @Override
                    public void onClick() {
                        scroll(1);
                    }
                }).set(new CenterConstraint(), new RelativeConstraint(0), new PixelConstraint(32), new PixelConstraint(32)).setOffset(new CenterOffset(), new CenterOffset())
        );
    }

    public void addRecipes(int... itemIDs) {
        clear();
        for(int itemID : itemIDs)
            this.recipes.addValuesWithKey(ItemManager.instance.getItem(itemID).recipeType, itemID);

        ArrayList<RecipeType> recipeTypes = new ArrayList<>(this.recipes.keySet());
        currentRecipeType = recipeTypes.get(0);

        addRecipeTypesToMenu(recipeTypes);
        updateArticles(0);
    }

    private void addRecipeTypesToMenu(ArrayList<RecipeType> recipeTypes) {
        int i = 0;
        for(RecipeType recipeType : RecipeType.values()) {
            if(!recipeTypes.contains(recipeType)) continue;
            this.menuBackground.addChild(new MenuSlot(recipeType.getPreviewTexture(), new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i++), new RelativeConstraint(0.5f)) {
                @Override
                public void onClick() {
                    currentRecipeType = recipeType;
                    updateArticles(currentRecipeIndex = 0);
                }
            }.setOffsetY(new CenterOffset()));
        }
    }

    public void updateArticles(int index) {
        ArrayList<Integer> recipesWithCurrentType = recipes.getValuesWithKey(currentRecipeType);
        for(int i = 0; i < VIEWABLE_RECIPES; i++) {
            int localIndex = index + i;
            if(localIndex == recipesWithCurrentType.size()) return;
            setRecipeGui(VIEWABLE_RECIPES - (i + 1), recipesWithCurrentType.get(localIndex));
        }
    }


    public void scroll(int amount) {
        currentRecipeIndex = Tools.clamp(currentRecipeIndex += amount, 0, recipes.getValuesWithKey(currentRecipeType).size() - (VIEWABLE_RECIPES - 1));
        updateArticles(currentRecipeIndex);
    }

    public void clear() {
        currentRecipeIndex = 0;
        menuBackground.clearChildren();
        for(GuiTransform backgroundRecipe : recipeGuis) {
            backgroundRecipe.setVisible(false);
            ((Gui) backgroundRecipe).clearChildren();
        }
    }

    private void setRecipeGui(int index, int itemID) {
        Gui recipeBackground = recipeGuis[index%VIEWABLE_RECIPES];
        Item currentItem = ItemManager.instance.getItem(itemID);
        recipeBackground.addChild(
                new Gui(currentItem.iconTexture.getString()).set(new PixelConstraint(0), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE)).setOffsetY(new CenterOffset()),
                new Text(currentItem.name.getString()).setFont(Font.Title).setLineLength(new PixelConstraint(1)).setColor(currentItem.rarity.getRarity().getColor()).setPosition(new PixelConstraint(Variables.ITEM_SIZE + Variables.SLOT_SPACE), new CenterConstraint()).setOffsetY(new CenterOffset()));
        recipeBackground.setVisible(true);
    }

}
