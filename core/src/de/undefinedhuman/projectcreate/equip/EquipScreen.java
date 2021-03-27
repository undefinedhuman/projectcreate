package de.undefinedhuman.projectcreate.equip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.items.ItemType;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.gui.Gui;
import de.undefinedhuman.projectcreate.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.inventory.InvTarget;
import de.undefinedhuman.projectcreate.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.utils.Utils;

public class EquipScreen extends Gui implements InvTarget {

    public static EquipScreen instance;

    private EquipSlot[] slots = new EquipSlot[4];
    private Vector2[] offset = new Vector2[] {new Vector2(0, 100), new Vector2(0, 68), new Vector2(-26, 40), new Vector2(0, -4)};
    private String[] texture = new String[] {"gui/preview/equip/Helmet-Preview.png", "gui/preview/equip/Chestplate-Preview.png", "gui/preview/equip/Arms-Preview.png", "gui/preview/equip/Boots-Preview.png"};
    private ItemType[] type = new ItemType[] { ItemType.HELMET, ItemType.ARMOR, ItemType.ARMOR, ItemType.ARMOR };

    public EquipScreen() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null) instance = this;
        setSize(Utils.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 5), Utils.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 10));
        setTitle("Character", Font.Title, Color.WHITE);

        /*addChild(new Gui("gui/preview/equip/Human-Preview.png").set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(64), new PixelConstraint(128)).setOffset(new CenterOffset(), new PixelOffset(48)));

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new EquipSlot(texture[i], type[i]) {
                @Override
                public void equip() { EquipManager.instance.equipItemNetwork(GameManager.instance.player, this.getItem().getID(), true); }

                @Override
                public void unequip() { EquipManager.instance.unEquipItemNetwork(GameManager.instance.player, this.getItem().getID(), true); }
            };
            slots[i].setPosition(new CenterConstraint(), new CenterConstraint()).setOffset(new PixelOffset(offset[i].x), new PixelOffset(offset[i].y));
        }
        addChild(slots);*/
        setVisible(false);
    }

    @Override
    public InvSlot getClickedSlot(OrthographicCamera camera) {
        if (!visible) return null;
        for (EquipSlot equipSlot : slots) if (equipSlot.isClicked(camera)) return equipSlot;
        return null;
    }

}
