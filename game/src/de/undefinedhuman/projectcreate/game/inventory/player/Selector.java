package de.undefinedhuman.projectcreate.game.inventory.player;

import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryType;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;
import de.undefinedhuman.projectcreate.core.network.packets.input.InputPacket;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.game.inventory.ClientInventory;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.network.ClientEncryption;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class Selector extends ClientInventory<SelectorInventory> {

    private static volatile Selector instance;

    private Selector() {
        super(GuiTemplate.HOTBAR, InventoryType.SELECTOR, "Selector");
        setPosition(new CenterConstraint(), new RelativeConstraint(1)).setOffset(new CenterOffset(), new PixelOffset(-Tools.getInventorySize(GuiTemplate.HOTBAR, 1) - 10));
        setVisible(true);
    }

    public int getSelectedItemID() {
        InvSlot item;
        if(inventory == null || linkInventory == null || (item = inventory[0][getSelectedIndex()]).isEmpty())
            return 0;
        return item.getID();
    }

    public void setSelected(int index) {
        if(InventoryManager.getInstance().isInventoryOpened() || inventory == null || linkInventory == null)
            return;
        inventory[0][getSelectedIndex()].setSelected(false);
        setSelectedIndex(index);
        inventory[0][getSelectedIndex()].setSelected(true);
        ClientManager.getInstance()
                .sendTCP(
                        InputPacket.createSelectionPacket(
                                ClientEncryption.getInstance().getAESEncryptionCipher(),
                                ClientManager.getInstance().currentSessionID,
                                getName(),
                                getSelectedIndex()
                        )
                );
    }

    public static Selector getInstance() {
        if(instance != null)
            return instance;
        synchronized (Selector.class) {
            if (instance == null)
                instance = new Selector();
        }
        return instance;
    }

}
