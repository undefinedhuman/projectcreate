package de.undefinedhuman.projectcreate.core.inventory;

import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

public class Inventory implements NetworkSerializable {

    protected InvItem[][] inventory;
    private int row, col;
    private String title;

    public Inventory(int row, int col, String title) {
        inventory = new InvItem[this.row = row][this.col = col];
        for (int i = 0; i < inventory.length; i++)
            for (int j = 0; j < inventory[i].length; j++)
                inventory[i][j] = new InvItem();
        this.title = title;
    }

    public int addItem(int id, int amount) {
        InvItem invItem = isFull(id);
        if (invItem == null)
            return amount;
        else {
            int newAmount = invItem.addItem(id, amount);
            return newAmount != 0 ? addItem(id, newAmount) : 0;
        }
    }

    public int removeItem(int id, int amountToBeRemoved) {
        if(id == 0) return 0;
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem currentItem = inventory[i][j];
                if(currentItem == null || currentItem.getID() != id)
                    continue;
                int amountOfCurrentSlot = currentItem.getAmount();
                inventory[i][j].removeItem(amountToBeRemoved);
                amountToBeRemoved = Math.max(amountToBeRemoved - amountOfCurrentSlot, 0);
                if(amountToBeRemoved == 0) return 0;
            }

        return amountToBeRemoved;
    }

    public boolean isFull(int id, int amount) {
        int currentAmount = 0, maxAmount = ItemManager.getInstance().getItem(id).maxAmount.getValue();

        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem currentItem = inventory[i][j];
                if(!currentItem.isTypeCompatible(id) || currentItem.getID() != id || currentItem.getAmount() == maxAmount)
                    continue;
                if(currentItem.isEmpty()) {
                    currentAmount += maxAmount;
                    continue;
                }
                currentAmount += maxAmount - currentItem.getAmount();
            }

        return amount - currentAmount > 0;
    }

    public InvItem isFull(int id) {
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem invItem = inventory[i][j];
                if(invItem.isEmpty() || (invItem.getID() == id && invItem.getAmount() < ItemManager.getInstance().getItem(id).maxAmount.getValue())) return invItem;
            }
        return null;
    }

    public int amountOf(int id) {
        int total = 0;
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem invItem = inventory[i][j];
                if(invItem.isEmpty() && invItem.getID() == id)
                    total += invItem.getAmount();
            }
        return total;
    }

    public boolean contains(int id) {
        return this.contains(id, 1);
    }

    public boolean contains(int id, int amount) {
        outer: for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem invItem = inventory[i][j];
                if(invItem.isEmpty() || invItem.getID() != id) continue;
                amount -= invItem.getAmount();
                if(amount <= 0) break outer;
            }
        return amount <= 0;
    }

    public InvItem getInvItem(int i, int j) {
        return inventory[i][j];
    }

    public InvItem[][] getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public void serialize(LineWriter writer) {
        writer.writeInt(row).writeInt(col);
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                writer.writeInt(inventory[i][j].getID()).writeInt(inventory[i][j].getAmount());
    }

    @Override
    public void parse(LineSplitter splitter) {
        int row = splitter.getNextInt(), col = splitter.getNextInt();
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                inventory[i][j].setItem(splitter.getNextInt(), splitter.getNextInt());
    }
}
