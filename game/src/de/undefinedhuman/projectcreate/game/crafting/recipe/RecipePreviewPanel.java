package de.undefinedhuman.projectcreate.game.crafting.recipe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.elements.scrollpanel.PoolableScrollPanel;
import de.undefinedhuman.projectcreate.engine.gui.event.ClickListener;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.engine.utils.Colors;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class RecipePreviewPanel extends Gui {

    private int currentItemID = -1;

    private Gui itemPreview;
    private Text name, category, description;
    private PoolableScrollPanel<IngredientGui> ingredients;
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
                                .setFont(Font.NORMAL)
                                .setLineLength(new RelativeConstraint(1f))
                                .setColor(Colors.WHITE)
                                .setPosition(new RelativeConstraint(0f), new RelativeConstraint(1f))
                                .setOffsetY(new RelativeOffset(-1f)),
                        category = (Text) new Text("Category")
                                .setFont(Font.NORMAL)
                                .setLineLength(new RelativeConstraint(1f))
                                .setFontSize(8)
                                .setColor(Color.WHITE)
                                .setPosition(new RelativeConstraint(0f), new RelativeConstraint(1f, -18)),
                        description = (Text) new Text("Description")
                                .setFont(Font.NORMAL)
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
                    Item item = ItemManager.getInstance().getItem(currentItemID);
                    // InventoryManager.getInstance().craftItem(currentItemID, item.recipeQuantity.getValue(), item.recipeItems.getValue().values());
                })
                .set(new RelativeConstraint(0.975f), new RelativeConstraint(0.025f), new PixelConstraint(50), new PixelConstraint(Variables.SLOT_SIZE))
                .setOffsetX(new RelativeOffset(-1f));

        ingredients = new PoolableScrollPanel<>(GuiTemplate.HOTBAR, IngredientGui::new);
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
        Item currentItem = ItemManager.getInstance().getItem(itemID);
        itemPreview.setTexture(currentItem.previewTexture.getValue());
        for(RecipeItem item : currentItem.recipeItems.getValue().values())
                ingredients.addContent().update(item);
        name
                .setText(currentItem.name.getValue())
                .setColor(currentItem.rarity.getValue().getColor());
        category.setText(currentItem.type.getTitle());
        description.setText(currentItem.desc.getValue());
        resize();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
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