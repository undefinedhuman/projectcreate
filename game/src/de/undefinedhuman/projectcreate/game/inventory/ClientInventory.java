package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryType;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ClientInventory<T extends Inventory> extends Gui implements InvTarget {

    protected InvSlot[][] inventory;
    protected T linkInventory;
    private InventoryType type;
    private String name;

    public ClientInventory(GuiTemplate template, InventoryType type, String name) {
        super(template);
        this.type = type;
        this.name = name;
    }

    public void linkInventory(Entity entity) {
        InventoryComponent component = Mappers.INVENTORY.get(entity);
        if(component == null || !component.hasInventory(name)) return;
        T inventory = component.getInventory(type, name);
        if(inventory == null) return;
        linkInventory(inventory);
    }

    public void linkInventory(T linkInventory) {
        unlink();
        if(linkInventory == null)
            return;
        this.linkInventory = linkInventory;
        this.inventory = new InvSlot[linkInventory.getRow()][linkInventory.getCol()];
        setSize(Tools.getInventorySize(getBaseCornerSize(), linkInventory.getCol()), Tools.getInventorySize(getBaseCornerSize(), linkInventory.getRow()));
        for (int i = linkInventory.getRow() - 1; i >= 0; i--)
            for (int j = 0; j < linkInventory.getCol(); j++) {
                InvSlot invSlot = InventoryManager.getInstance().getClientInvSlotPool().get();
                invSlot.link(i, j, linkInventory.getInvItem(i, j));
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

    public int getCol() {
        if(linkInventory == null)
            return 0;
        return linkInventory.getCol();
    }

}