package de.undefinedhuman.projectcreate.core.crafting.recipe;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.engine.utils.ds.Poolable;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.core.crafting.CraftingInventory;
import de.undefinedhuman.projectcreate.engine.items.Item;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.gui.Gui;
import de.undefinedhuman.projectcreate.core.gui.event.ClickListener;
import de.undefinedhuman.projectcreate.core.gui.text.Text;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.core.item.ItemManager;

public class RecipeGui extends Gui implements Poolable {

    public static final int CHILD_AMOUNT = 2;

    private Gui icon;
    private Text name;
    private int itemID;

    public RecipeGui() {
        super();
        set(new PixelConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new PixelConstraint(Variables.SLOT_SIZE));

        addListener((ClickListener) () -> CraftingInventory.instance.updateRecipe(itemID));

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
                                        .setLineLength(new RelativeConstraint(1f))
                                        .setColor(Color.WHITE)
                                        .setPosition(new PixelConstraint(Variables.SLOT_SPACE), new CenterConstraint())
                                        .setOffsetY(new CenterOffset())
                        )
                        .set(new PixelConstraint(Variables.SLOT_SIZE + Variables.SLOT_SPACE), new PixelConstraint(0), new RelativeConstraint(1f, -(Variables.SLOT_SIZE + Variables.SLOT_SPACE)), new PixelConstraint(Variables.SLOT_SIZE))
        );
    }

    public RecipeGui update(int itemID) {
        this.itemID = itemID;
        Item currentItem = ItemManager.instance.getItem(itemID);
        this.icon.setTexture(currentItem.iconTexture.getString());
        this.name
                .setText(currentItem.name.getString())
                .setColor(currentItem.rarity.getRarity().getColor());
        return this;
    }

    @Override
    public boolean validate() {
        return getChildren().size() == CHILD_AMOUNT && icon != null && name != null;
    }
}