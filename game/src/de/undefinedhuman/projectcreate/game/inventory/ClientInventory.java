package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;

public class ClientInventory implements InvTarget {

    private ClientInvSlot[][] inventory;
    private int row, col;

    public ClientInventory(int row, int col) {
        resizeInventory(row, col);
    }

    public void resizeInventory(int row, int col) {
        delete();
        if((this.row == row && this.col == col) || row < 0 || col < 0)
            return;
        this.inventory = new ClientInvSlot[this.row = row][this.col = col];

    }

    public void delete() {
        if(inventory == null) return;
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                if()
            }
    }

    @Override
    public InvSlot getClickedSlot(OrthographicCamera camera) {
        return null;
    }
}
