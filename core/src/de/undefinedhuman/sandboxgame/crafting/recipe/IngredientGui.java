package de.undefinedhuman.sandboxgame.crafting.recipe;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.engine.crafting.RecipeItem;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.Colors;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;

public class IngredientGui extends Gui {

    public static final int CHILD_AMOUNT = 2;

    private Gui icon;
    private Text name, amount;

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
                                        .setColor(Color.WHITE)
                                        .setPosition(new PixelConstraint(Variables.SLOT_SPACE), new RelativeConstraint(0.95f))
                                        .setOffsetY(new RelativeOffset(-1f)),
                                amount = (Text) new Text("0/0")
                                        .setFont(Font.Normal)
                                        .setFontSize(10)
                                        .setColor(Color.WHITE)
                                        .setPosition(new RelativeConstraint(1f, -Variables.SLOT_SPACE), new RelativeConstraint(0.05f))
                                        .setOffsetX(new RelativeOffset(-1f))
                        )
                        .set(new PixelConstraint(Variables.SLOT_SIZE + Variables.SLOT_SPACE), new PixelConstraint(0), new RelativeConstraint(1f, -(Variables.SLOT_SIZE + Variables.SLOT_SPACE)), new PixelConstraint(Variables.SLOT_SIZE))
        );
    }

    public IngredientGui update(RecipeItem item) {
        int id = Integer.parseInt(item.getKey());
        int amount = item.quantity.getInt();
        Item currentItem = ItemManager.instance.getItem(id);
        this.icon.setTexture(currentItem.iconTexture.getString());
        this.name
                .setText(currentItem.name.getString())
                .setColor(currentItem.rarity.getRarity().getColor());
        int totalItemsInInventory = InventoryManager.instance.amountOf(id);
        this.amount
                .setText(totalItemsInInventory + "/" + amount)
                .setColor(totalItemsInInventory >= amount ? Colors.LIGHT_GREEN : Colors.RED);
        return this;
    }

}
