package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.utils.ds.ObjectPool;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ClientInventory extends Gui implements InvTarget {

    private ClientInvSlot[][] inventory;
    private int row, col;

    // TODO FIX SETSIZE

    public ClientInventory(GuiTemplate template) {
        super(template);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public void linkInventory(ObjectPool<ClientInvSlot> slotObjectPool, Inventory linkInventory) {
        delete(slotObjectPool);
        if(linkInventory == null)
            return;
        this.inventory = new ClientInvSlot[this.row = linkInventory.getRow()][this.col = linkInventory.getCol()];
        setSize(Tools.getInventoryConstraint(getTexture(), col), Tools.getInventoryConstraint(getTexture(), row));
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                ClientInvSlot invSlot = slotObjectPool.get();
                invSlot.link(i, j, linkInventory.getInvItem(i, j));
                inventory[i][j] = invSlot;
                addChild(invSlot);
            }
        setVisible(false);
    }

    public void delete(ObjectPool<ClientInvSlot> slotObjectPool) {
        if(inventory == null) return;
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                ClientInvSlot invSlot = inventory[i][j];
                if(invSlot == null)
                    continue;
                slotObjectPool.add(invSlot);
                inventory[i][j] = null;
            }
        inventory = null;
        removeChildren();
    }

    @Override
    public ClientInvSlot getClickedSlot(OrthographicCamera camera) {
        if (!visible) return null;
        for (ClientInvSlot[] invSlots : inventory)
            for (ClientInvSlot invSlot : invSlots)
                if (invSlot.isClicked(camera))
                    return invSlot;
        return null;
    }

}