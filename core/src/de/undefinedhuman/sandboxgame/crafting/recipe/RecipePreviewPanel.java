package de.undefinedhuman.sandboxgame.crafting.recipe;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.engine.crafting.RecipeItem;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.Colors;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.elements.scrollpanel.ScrollPanel;
import de.undefinedhuman.sandboxgame.gui.event.ClickListener;
import de.undefinedhuman.sandboxgame.gui.pool.GuiPool;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class RecipePreviewPanel extends Gui {

    private int currentItemID = -1;

    private Gui itemPreview;
    private Text name, category, description;
    private ScrollPanel ingredients;
    private GuiPool<IngredientGui> ingredientGuiPool;
    private boolean childrenVisible;

    public RecipePreviewPanel() {
        super(GuiTemplate.HOTBAR);
        set(new RelativeConstraint(1), new PixelConstraint(0), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 9), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 8));
        setOffsetX(new RelativeOffset(-1f));

        int itemPreviewSize = 48 + GuiTemplate.SLOT.cornerSize * 2;

        Gui previewBackground = (Gui) new Gui(GuiTemplate.SLOT)
                .addChild(
                        itemPreview = (Gui) new Gui()
                                .set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(48), new PixelConstraint(48))
                                .setOffset(new CenterOffset(), new CenterOffset())
                )
                .set(new RelativeConstraint(0.025f), new RelativeConstraint(0.975f), new PixelConstraint(itemPreviewSize), new PixelConstraint(itemPreviewSize))
                .setOffsetY(new RelativeOffset(-1f));

        Gui itemInfoPanel = (Gui) new Gui()
                .addChild(
                        name = (Text) new Text("Unknown")
                                .setFont(Font.Normal)
                                .setLineLength(new RelativeConstraint(1f))
                                .setColor(Colors.WHITE)
                                .setPosition(new RelativeConstraint(0f), new RelativeConstraint(1f))
                                .setOffsetY(new RelativeOffset(-1f)),
                        category = (Text) new Text("Category")
                                .setFont(Font.Normal)
                                .setLineLength(new RelativeConstraint(1f))
                                .setFontSize(8)
                                .setColor(Color.WHITE)
                                .setPosition(new RelativeConstraint(0f), new RelativeConstraint(1f, -18)),
                        description = (Text) new Text("Description")
                                .setFont(Font.Normal)
                                .setLineLength(new RelativeConstraint(1f))
                                .setWrap(true)
                                .setFontSize(10)
                                .setColor(Color.WHITE)
                                .setPosition(new RelativeConstraint(0f), new PixelConstraint(12))
                )
                .set(new RelativeConstraint(0.025f, itemPreviewSize + GuiTemplate.SLOT.cornerSize), new RelativeConstraint(0.975f, -GuiTemplate.SLOT.cornerSize), new RelativeConstraint(0.95f, -itemPreviewSize), new PixelConstraint(48))
                .setOffsetY(new RelativeOffset(-1f));

        Gui craftButton = (Gui) new Gui(GuiTemplate.SLOT)
                .addChild(
                        new Text("Craft")
                                .setPosition(new CenterConstraint(), new CenterConstraint())
                                .setOffset(new CenterOffset(), new CenterOffset())
                )
                .addListener((ClickListener) () -> {
                    Item item = ItemManager.instance.getItem(currentItemID);
                    InventoryManager.instance.craftItem(currentItemID, item.recipeQuantity.getInt(), item.recipeItems.values());
                })
                .set(new RelativeConstraint(0.975f), new RelativeConstraint(0.025f), new PixelConstraint(50), new PixelConstraint(Variables.SLOT_SIZE))
                .setOffsetX(new RelativeOffset(-1f));

        ingredientGuiPool = new GuiPool<>(IngredientGui::new, 300000);
        ingredients = new ScrollPanel(GuiTemplate.HOTBAR);
        ingredients
                .set(new CenterConstraint(), new RelativeConstraint(0.675f), new RelativeConstraint(0.95f), new RelativeConstraint(0.5f))
                .setOffset(new CenterOffset(), new RelativeOffset(-1f));

        addChild(previewBackground, craftButton, ingredients, itemInfoPanel);
        setChildrenVisible(false);
    }

    public void update(int itemID) {
        setChildrenVisible(true);
        currentItemID = itemID;
        ingredients.clear();
        Item currentItem = ItemManager.instance.getItem(itemID);
        itemPreview.setTexture(currentItem.previewTexture.getString());
        for(RecipeItem item : currentItem.recipeItems.values())
            ingredients.addContent(ingredientGuiPool.get().update(item));
        name
                .setText(currentItem.name.getString())
                .setColor(currentItem.rarity.getRarity().getColor());
        category.setText(currentItem.type.getTitle());
        description.setText(currentItem.desc.getString());
        resize();
    }

    public void setChildrenVisible(boolean childrenVisible) {
        this.childrenVisible = childrenVisible;
        for(GuiTransform children : getChildren())
            children.setVisible(childrenVisible);
    }

    @Override
    public GuiTransform setVisible(boolean visible) {
        super.setVisible(visible);
        setChildrenVisible(childrenVisible);
        return this;
    }

}