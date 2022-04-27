package de.undefinedhuman.projectcreate.core.inventory;

import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

public class SlotInfo {

    private long linkedEntityID;
    private Vector2i currentInventoryPosition = new Vector2i();
    private String linkInventoryName;

    public SlotInfo() {
        unlink();
    }

    public int getRow() {
        return currentInventoryPosition.x;
    }

    public int getCol() {
        return currentInventoryPosition.y;
    }

    public long getLinkedEntityID() {
        return linkedEntityID;
    }

    public String getLinkInventoryName() {
        return linkInventoryName;
    }

    public void link(long entityID, int row, int col, String inventoryName) {
        this.linkedEntityID = entityID;
        this.currentInventoryPosition.set(row, col);
        this.linkInventoryName = inventoryName;
    }

    public void unlink() {
        this.linkedEntityID = -1;
        this.currentInventoryPosition.setZero();
        this.linkInventoryName = "";
    }

}
