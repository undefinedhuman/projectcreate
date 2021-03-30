package de.undefinedhuman.projectcreate.core.crafting.recipe;

import de.undefinedhuman.projectcreate.core.engine.utils.ds.Poolable;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.core.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.core.engine.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.core.engine.items.Item;
import de.undefinedhuman.projectcreate.core.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.core.engine.utils.Colors;
import de.undefinedhuman.projectcreate.core.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.gui.Gui;
import de.undefinedhuman.projectcreate.core.gui.text.Text;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.core.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.core.item.ItemManager;
import de.undefinedhuman.projectcreate.core.item.listener.ItemChangeListener;

public class IngredientGui extends Gui implements Poolable {

    public static final int CHILD_AMOUNT = 2;

    private Gui icon;
    private Text name, amount;
    private ItemChangeListener listener;
    private int currentItemID = -1, currentAmount = 0;

    public IngredientGui() {
        super();
        set(new PixelConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new PixelConstraint(Variables.SLOT_SIZE));

        addChild(
                new Gui(GuiTemplate.SLOT)
                        .addChild(
                                icon = (Gui) new Gui("Unknown.png")
                                        .set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE))
                                        .setOffset(new CenterOffset(), new CenterOffset())
                        )
                        .set(new PixelConstraint(0), new PixelConstraint(0), new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE)),
                new Gui(GuiTemplate.SLOT)
                        .addChild(
                                name = (Text) new Text("Unknown")
                                        .setFont(Font.Normal)
                                        .setFontSize(10)
                                        .setLineLength(new RelativeConstraint(1f))
                                        .setPosition(new PixelConstraint(Variables.SLOT_SPACE), new RelativeConstraint(0.95f))
                                        .setOffsetY(new RelativeOffset(-1f)),
                                amount = (Text) new Text("0/0")
                                        .setFont(Font.Normal)
                                        .setFontSize(10)
                                        .setPosition(new RelativeConstraint(1f, -Variables.SLOT_SPACE), new RelativeConstraint(0.05f))
                                        .setOffsetX(new RelativeOffset(-1f))
                        )
                        .set(new PixelConstraint(Variables.SLOT_SIZE + Variables.SLOT_SPACE), new PixelConstraint(0), new RelativeConstraint(1f, -(Variables.SLOT_SIZE + Variables.SLOT_SPACE)), new PixelConstraint(Variables.SLOT_SIZE))
        );

        listener = amount -> updateAmountText(currentItemID, currentAmount);
    }

    @Override
    public boolean validate() {
        return getChildren().size() == CHILD_AMOUNT && icon != null && name != null && amount != null;
    }

    @Override
    public void delete() {
        super.delete();
        InventoryManager.instance.removeListener(currentItemID, listener);
    }

    public IngredientGui update(RecipeItem item) {
        InventoryManager.instance.removeListener(currentItemID, listener);
        currentItemID = Integer.parseInt(item.getKey());
        currentAmount = item.quantity.getInt();
        Item currentItem = ItemManager.instance.getItem(currentItemID);
        this.icon.setTexture(currentItem.iconTexture.getString());
        this.name
                .setText(currentItem.name.getString())
                .setColor(currentItem.rarity.getRarity().getColor());
        updateAmountText(currentItemID, currentAmount);
        InventoryManager.instance.addListener(currentItemID, listener);
        return this;
    }

    private void updateAmountText(int id, int amount) {
        int totalItemsInInventory = InventoryManager.instance.amountOf(id);
        this.amount
                .setText(totalItemsInInventory + "/" + amount)
                .setColor(totalItemsInInventory >= amount ? Colors.LIGHT_GREEN : Colors.RED);
    }

}
