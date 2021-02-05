package de.undefinedhuman.sandboxgame.crafting.recipe;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.item.ItemManager;

public abstract class RecipeGui extends Gui {

    public static final int CHILD_AMOUNT = 2;

    private Gui icon;
    private Text name;
    private int itemID;

    public RecipeGui() {
        super(GuiTemplate.SLOT);
        set(new PixelConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new PixelConstraint(Variables.SLOT_SIZE));

        addEvent(new ClickEvent() {
            @Override
            public void onClick() {
                RecipeGui.this.onClick(itemID);
            }
        });

        addChild(
                icon = (Gui) new Gui("Unknown.png")
                        .set(new PixelConstraint(0), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE))
                        .setOffsetY(new CenterOffset()),
                name = (Text) new Text("Unknown")
                        .setFont(Font.Title)
                        .setLineLength(new PixelConstraint(1))
                        .setColor(Color.WHITE)
                        .setPosition(new PixelConstraint(Variables.ITEM_SIZE + Variables.SLOT_SPACE), new CenterConstraint())
                        .setOffsetY(new CenterOffset())
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

    public abstract void onClick(int itemID);

}
