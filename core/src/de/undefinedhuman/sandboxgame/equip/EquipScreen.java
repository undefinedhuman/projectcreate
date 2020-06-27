package de.undefinedhuman.sandboxgame.equip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.sandboxgame.inventory.EquipSlot;
import de.undefinedhuman.sandboxgame.inventory.InvTarget;
import de.undefinedhuman.sandboxgame.inventory.Slot;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class EquipScreen extends Gui implements InvTarget {

    public static EquipScreen instance;

    private EquipSlot[] slots = new EquipSlot[4];
    private Vector2[] offset = new Vector2[] {new Vector2(0, 100), new Vector2(0, 68), new Vector2(-26, 40), new Vector2(0, -4)};
    private String[] texture = new String[] {"gui/preview/equip/Helmet-Preview.png", "gui/preview/equip/Chestplate-Preview.png", "gui/preview/equip/Arms-Preview.png", "gui/preview/equip/Boots-Preview.png"};
    private ItemType[] type = new ItemType[] { ItemType.HELMET, ItemType.ARMOR, ItemType.ARMOR, ItemType.ARMOR };

    public EquipScreen() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null) instance = this;
        setSize(new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 5)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10)));
        setTitle("Equip", Font.Title, Color.WHITE);

        addChild(new Gui("gui/preview/equip/Human-Preview.png").set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(64), new PixelConstraint(128)).setOffset(new CenterOffset(), new PixelOffset(48)));

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new EquipSlot(texture[i], type[i]) {
                @Override
                public void equip() { EquipManager.instance.equipItemNetwork(GameManager.instance.player, this.getItem().getID(), true); }

                @Override
                public void unequip() { EquipManager.instance.unEquipItemNetwork(GameManager.instance.player, this.getItem().getID(), true); }
            };
            slots[i].setPosition(new CenterConstraint(), new CenterConstraint()).setOffset(new PixelOffset(offset[i].x), new PixelOffset(offset[i].y));
        }
        addChild(slots);
        setVisible(false);
    }

    @Override
    public Slot getClickedSlot(OrthographicCamera camera) {
        if (!visible) return null;
        for (EquipSlot equipSlot : slots) if (equipSlot.isClicked(camera)) return equipSlot;
        return null;
    }

}
