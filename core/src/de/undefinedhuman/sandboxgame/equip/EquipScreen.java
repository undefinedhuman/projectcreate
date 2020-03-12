package de.undefinedhuman.sandboxgame.equip;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.inventory.EquipSlot;
import de.undefinedhuman.sandboxgame.inventory.InvTarget;
import de.undefinedhuman.sandboxgame.inventory.Slot;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class EquipScreen extends Gui implements InvTarget {

    private EquipSlot[] slots = new EquipSlot[4];
    private Vector2[] offset = new Vector2[] {new Vector2(0, 102), new Vector2(0, 68), new Vector2(-26, 40), new Vector2(0, -4)};
    private String[] texture = new String[] {"gui/preview/equip/Helmet-Preview.png", "gui/preview/equip/Chestplate-Preview.png", "gui/preview/equip/Arms-Preview.png", "gui/preview/equip/Boots-Preview.png"};
    private ItemType[] type = new ItemType[] {ItemType.HELMET, ItemType.ARMOR, ItemType.ARMOR, ItemType.ARMOR};

    public EquipScreen() {
        super(GuiTemplate.SMALL_PANEL);
        setScale(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 5), Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10));
        Gui gui = new Gui("gui/preview/equip/Human-Preview.png");
        gui.set("r0.5", "r0.5", "p64", "p128").setOffsetY("p48").setCentered();
        addChild(gui);

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new EquipSlot(texture[i], type[i]) {
                @Override
                public void equip() { EquipManager.instance.equipItemNetwork(GameManager.instance.player, this.getItem().getID(), true); }

                @Override
                public void unequip() { EquipManager.instance.unEquipItemNetwork(GameManager.instance.player, this.getItem().getID(), true); }
            };
            slots[i].setPosition("r0.5", "r0.5").setOffset("p" + offset[i].x, "p" + offset[i].y).setCentered();
        }
        addChild(slots);
    }

    @Override
    public Slot getClickedSlot(OrthographicCamera camera) {
        if (!visible) return null;
        for (EquipSlot equipSlot : slots) if (equipSlot.isClicked(camera)) return equipSlot;
        return null;
    }

}
