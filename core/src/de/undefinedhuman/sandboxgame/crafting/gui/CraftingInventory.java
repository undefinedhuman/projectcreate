package de.undefinedhuman.sandboxgame.crafting.gui;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.MultiMap;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.text.Text;
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

    private Gui menuBackground, recipeBackground, recipePreviewTexture;
    private ScrollPanel recipesBackground;

    private MultiMap<RecipeType, Gui> recipes = new MultiMap<>();
    private RecipeType currentRecipeType = null;

    public CraftingInventory() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null) instance = this;
        set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 15)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10)));
        setOffset(new CenterOffset(), new CenterOffset());
        initBackgrounds();
        setRecipes(1, 2);
        setTitle("Crafting", Font.Title, Color.WHITE);
    }

    private void initBackgrounds() {
        addChild(
                menuBackground = (Gui) new Gui(GuiTemplate.HOTBAR)
                        .set(new PixelConstraint(0), new RelativeConstraint(1), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.HOTBAR, 1)))
                        .setOffsetY(new RelativeOffset(-1)),
                recipesBackground = (ScrollPanel) new ScrollPanel()
                        .setSize(new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.HOTBAR, 8))),
                recipeBackground = (Gui) new Gui(GuiTemplate.HOTBAR)
                        .addChild(recipePreviewTexture = (Gui) new Gui("Unknown.png")
                                .set(new CenterConstraint(), new RelativeConstraint(0.75f), new PixelConstraint(64), new PixelConstraint(64))
                                .setOffset(new CenterOffset(), new CenterOffset())
                        )
                        .set(new RelativeConstraint(1), new PixelConstraint(0), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.HOTBAR, 7)), new RelativeConstraint(1f))
                        .setOffsetX(new RelativeOffset(-1f))
        );
    }

    public void setRecipes(int... itemIDs) {
        clear();
        for(int itemID : itemIDs) {
            Item currentItem = ItemManager.instance.getItem(itemID);
            Gui itemRecipeGui = new Gui(GuiTemplate.SLOT)
                    .addChild(
                            new Gui(currentItem.iconTexture.getString())
                                    .set(new PixelConstraint(0), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE))
                                    .setOffsetY(new CenterOffset()),
                            new Text(currentItem.name.getString())
                                    .setFont(Font.Title)
                                    .setLineLength(new PixelConstraint(1))
                                    .setColor(currentItem.rarity.getRarity().getColor())
                                    .setPosition(new PixelConstraint(Variables.ITEM_SIZE + Variables.SLOT_SPACE), new CenterConstraint())
                                    .setOffsetY(new CenterOffset())
                    );
            this.recipes.add(ItemManager.instance.getItem(itemID).recipeType, itemRecipeGui);
        }

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
                    currentRecipeType = recipeType;
                    updateRecipes();
                }
            }.setOffsetY(new CenterOffset()));
        }
    }

    public void updateRecipes() {
        recipesBackground.setContent(Variables.SLOT_SIZE, Variables.SLOT_SPACE, recipes.getValuesWithKey(currentRecipeType).toArray(new Gui[0]));
    }

    public void updateRecipe() {

    }

    public void clear() {
        menuBackground.deleteChildren();
        recipesBackground.clear();
    }

}
