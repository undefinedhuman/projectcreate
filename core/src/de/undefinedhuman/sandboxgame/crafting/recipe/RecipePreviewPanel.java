package de.undefinedhuman.sandboxgame.crafting.recipe;

import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class RecipePreviewPanel extends Gui {

    private Gui itemPreview, itemIcon;

    private boolean childrenVisible;

    public RecipePreviewPanel() {
        super(GuiTemplate.HOTBAR);
        set(new RelativeConstraint(1), new PixelConstraint(0), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 7), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 8));
        setOffsetX(new RelativeOffset(-1f));

        itemPreview = (Gui) new Gui("Unknown.png")
                .set(new CenterConstraint(), new RelativeConstraint(0.75f), new PixelConstraint(64), new PixelConstraint(64))
                .setOffset(new CenterOffset(), new CenterOffset());
        Gui itemIconBackground = (Gui) new Gui(GuiTemplate.SLOT)
                .set(new PixelConstraint(0), new RelativeConstraint(1f), new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE))
                .setOffsetY(new RelativeOffset(-1f));
        itemIconBackground.addChild(
                itemIcon = (Gui) new Gui("Unknown.png")
                        .set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE))
                        .setOffset(new CenterOffset(), new CenterOffset())
        );

        addChild(itemIconBackground, itemPreview);
        setChildrenVisible(false);
    }

    public void update(int itemID) {
        setChildrenVisible(true);
        Item currentItem = ItemManager.instance.getItem(itemID);
        itemPreview.setTexture(currentItem.previewTexture.getString());
        itemIcon.setTexture(currentItem.iconTexture.getString());
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