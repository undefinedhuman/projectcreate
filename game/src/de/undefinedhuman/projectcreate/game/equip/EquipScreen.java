package de.undefinedhuman.projectcreate.game.equip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.game.gui.Gui;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.inventory.InvTarget;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class EquipScreen extends Gui implements InvTarget {

    private static volatile EquipScreen instance;

    private EquipSlot[] slots = new EquipSlot[4];
    private Vector2[] offset = new Vector2[] {new Vector2(0, 100), new Vector2(0, 68), new Vector2(-26, 40), new Vector2(0, -4)};
    private String[] texture = new String[] {"gui/preview/equip/Helmet-Preview.png", "gui/preview/equip/Chestplate-Preview.png", "gui/preview/equip/Arms-Preview.png", "gui/preview/equip/Boots-Preview.png"};
    private ItemType[] type = new ItemType[] { ItemType.HELMET, ItemType.ARMOR, ItemType.ARMOR, ItemType.ARMOR };

    public EquipScreen() {
        super(GuiTemplate.SMALL_PANEL);
        setSize(Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 5), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 10));
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
        for (EquipSlot equipSlot : slots)
            if (equipSlot.isClicked(camera))
                return equipSlot;
        return null;
    }

    public static EquipScreen getInstance() {
        if (instance == null) {
            synchronized (EquipScreen.class) {
                if (instance == null)
                    instance = new EquipScreen();
            }
        }
        return instance;
    }

}
