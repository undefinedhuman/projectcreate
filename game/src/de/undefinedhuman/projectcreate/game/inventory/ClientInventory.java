package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryType;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ClientInventory<T extends Inventory> extends Gui implements InvTarget {

    protected InvSlot[][] inventory;
    protected T linkInventory;
    private InventoryType type;
    private long linkedEntityID = 0;
    private String name;

    public ClientInventory(GuiTemplate template, InventoryType type, String name) {
        super(template);
        this.type = type;
        this.name = name;
    }

    public void linkInventory(Entity entity) {
        IDComponent idComponent = Mappers.ID.get(entity);
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(entity);
        if(idComponent == null || inventoryComponent == null || !inventoryComponent.hasInventory(name)) return;
        T inventory = inventoryComponent.getInventory(type, name);
        if(inventory == null) return;
        linkInventory(idComponent.getWorldID(), inventory);
    }

    public void linkInventory(long linkedEntityID, T linkInventory) {
        unlink();
        if(linkInventory == null)
            return;
        this.linkedEntityID = linkedEntityID;
        this.linkInventory = linkInventory;
        this.inventory = new InvSlot[linkInventory.getRow()][linkInventory.getCol()];
        setSize(Tools.getInventorySize(getBaseCornerSize(), linkInventory.getCol()), Tools.getInventorySize(getBaseCornerSize(), linkInventory.getRow()));
        for (int i = linkInventory.getRow() - 1; i >= 0; i--)
            for (int j = 0; j < linkInventory.getCol(); j++) {
                InvSlot invSlot = InventoryManager.getInstance().getClientInvSlotPool().get();
                invSlot.link(linkedEntityID, linkInventory.getTitle(), i, j, linkInventory.getInvItem(i, j));
                inventory[i][j] = invSlot;
                addChild(invSlot);
            }
        resize();
    }

    @Override
    public void delete() {
        super.delete();
        unlink();
    }

    public void unlink() {
        if(linkInventory == null || inventory == null)
            return;
        for (int i = inventory.length - 1; i >= 0; i--)
            for (int j = 0; j < inventory[0].length; j++) {
                InvSlot invSlot = inventory[i][j];
                if(invSlot == null)
                    continue;
                InventoryManager.getInstance().getClientInvSlotPool().add(invSlot);
                inventory[i][j] = null;
            }
        linkedEntityID = -1;
        inventory = null;
        linkInventory = null;
        removeChildren();
    }

    @Override
    public InvSlot getClickedSlot(OrthographicCamera camera) {
        if (!visible || linkInventory == null)
            return null;
        for (InvSlot[] invSlots : inventory)
            for (InvSlot invSlot : invSlots)
                if (invSlot.isClicked(camera))
                    return invSlot;
        return null;
    }

    protected void setSelectedIndex(int index) {
        if(linkInventory == null || !(linkInventory instanceof SelectorInventory))
            return;
        ((SelectorInventory) linkInventory).selectedIndex = Utils.clamp(index, 0, linkInventory.getCol()-1);
    }

    public int getSelectedIndex() {
        if(linkInventory == null || !(linkInventory instanceof SelectorInventory))
            return 0;
        return ((SelectorInventory) linkInventory).selectedIndex;
    }

    public String getName() {
        return name;
    }

    public long getLinkedEntityID() {
        return linkedEntityID;
    }

    public int getCol() {
        if(linkInventory == null)
            return 0;
        return linkInventory.getCol();
    }

}