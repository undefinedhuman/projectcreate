package de.undefinedhuman.projectcreate.game.crafting.recipe;

import de.undefinedhuman.projectcreate.core.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.engine.utils.Colors;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.ds.Poolable;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.game.inventory.listener.ItemChangeListener;

public class IngredientGui extends Gui implements Poolable {

    public static final int CHILD_AMOUNT = 2;

    private Gui icon;
    private Text name, amount;
    private ItemChangeListener listener;
    private int ingredientID = -1, amountNeededToCraft = 0, amountInInventory = 0;

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
                                        .setFont(Font.NORMAL)
                                        .setFontSize(10)
                                        .setLineLength(new RelativeConstraint(1f))
                                        .setPosition(new PixelConstraint(Variables.SLOT_SPACE), new RelativeConstraint(0.95f))
                                        .setOffsetY(new RelativeOffset(-1f)),
                                amount = (Text) new Text("0/0")
                                        .setFont(Font.NORMAL)
                                        .setFontSize(10)
                                        .setPosition(new RelativeConstraint(1f, -Variables.SLOT_SPACE), new RelativeConstraint(0.05f))
                                        .setOffsetX(new RelativeOffset(-1f))
                        )
                        .set(new PixelConstraint(Variables.SLOT_SIZE + Variables.SLOT_SPACE), new PixelConstraint(0), new RelativeConstraint(1f, -(Variables.SLOT_SIZE + Variables.SLOT_SPACE)), new PixelConstraint(Variables.SLOT_SIZE))
        );

        listener = amount -> updateAmountText(ingredientID, amountInInventory = amountInInventory-amount, amountNeededToCraft);
        /*listener = new ItemChangeListener(currentItemID, InventoryManager.getInstance()) {
            @Override
            public void notify(int amount) {
                updateAmountText(ingredientID, amountInInventory = amountInInventory-amount, amountNeededToCraft)
            }
        };*/
    }

    @Override
    public boolean validate() {
        return getChildren().size() == CHILD_AMOUNT && icon != null && name != null && amount != null;
    }

    @Override
    public void delete() {
        super.delete();
        InventoryManager.getInstance().removeListener(ingredientID, listener);
    }

    public IngredientGui update(RecipeItem item) {
        InventoryManager.getInstance().removeListener(ingredientID, listener);
        ingredientID = item.getKey();
        amountNeededToCraft = item.quantity.getValue();
        Item currentItem = ItemManager.getInstance().getItem(ingredientID);
        this.icon.setTexture(currentItem.iconTexture.getValue());
        this.name
                .setText(currentItem.name.getValue())
                .setColor(currentItem.rarity.getValue().getColor());
        updateAmountText(ingredientID, amountInInventory = InventoryManager.getInstance().amountOf(ingredientID), amountNeededToCraft);
        InventoryManager.getInstance().addListener(ingredientID, listener);
        return this;
    }

    private void updateAmountText(int id, int amountInInventory, int amountToCraft) {
        this.amount
                .setText(amountInInventory + "/" + amountToCraft)
                .setColor(amountInInventory >= amountToCraft ? Colors.LIGHT_GREEN : Colors.RED);
    }

}
