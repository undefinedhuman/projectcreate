package de.undefinedhuman.projectcreate.game.equip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.game.inventory.InvTarget;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class EquipScreen extends Gui implements InvTarget {

    private static volatile EquipScreen instance;

    private EquipSlot[] slots = new EquipSlot[4];
    private Vector2[] offset = new Vector2[] {new Vector2(0, 67), new Vector2(0, 35), new Vector2(-26, 7), new Vector2(0, -37)};
    private String[] texture = new String[] {"gui/preview/equip/Helmet-Preview.png", "gui/preview/equip/Chestplate-Preview.png", "gui/preview/equip/Arms-Preview.png", "gui/preview/equip/Boots-Preview.png"};
    private ItemType[] type = new ItemType[] { ItemType.HELMET, ItemType.ARMOR, ItemType.ARMOR, ItemType.ARMOR };

    private EquipScreen() {
        super(GuiTemplate.SMALL_PANEL);
        setSize(Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 5), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 10));
        setTitle("Character", Font.Title, Color.WHITE);

        addChild(new Gui("gui/preview/equip/Human-Preview.png").set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(64), new PixelConstraint(128)).setOffset(new CenterOffset(), new PixelOffset(-48)));

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new EquipSlot(new CenterConstraint(), new CenterConstraint(), type[i], texture[i]) {
                @Override
                public void equip() {}

                @Override
                public void unEquip() {}
            };
            slots[i].setPosition(new RelativeConstraint(0.5f, (int) offset[i].x), new RelativeConstraint(0.5f, (int) offset[i].y)).setOffset(new CenterOffset(), new CenterOffset());
        }
        addChild(slots);
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
