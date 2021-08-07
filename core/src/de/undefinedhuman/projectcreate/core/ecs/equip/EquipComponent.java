package de.undefinedhuman.projectcreate.core.ecs.equip;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class EquipComponent implements Component, NetworkSerializable {

    public int[] itemIDs = new int[5];
    private Vector2[] itemOffsets, itemPositions;
    private String[] invisibleSprites;

    public EquipComponent(String[] invisibleSprites, Vector2[] itemOffsets, Vector2[] itemPositions) {
        for (int i = 0; i < itemIDs.length; i++) itemIDs[0] = -1;
        this.itemOffsets = itemOffsets;
        this.itemPositions = itemPositions;
        this.invisibleSprites = invisibleSprites;
    }

    public Vector2 getCurrentPosition(int index) {
        return itemPositions[index];
    }

    public Vector2 getCurrentOffset(int index) {
        if(!Utils.isInRange(index, 0, itemOffsets.length-1))
            return new Vector2();
        Vector2 vector = itemOffsets[index];
        return new Vector2(vector);
    }

    public String[] getInvisibleSprites() { return invisibleSprites; }

    public void setItemID(int index, int itemID) {
        if (!Utils.isInRange(index, 0, 4)) return;
        itemIDs[index] = itemID;
    }

    public void setItemID(int index, Item item) {
        if (!Utils.isInRange(index, 0, 4))
            return;
        // itemIDs[index] = itemID;
        // TODO ADD ARMOR
    }

    @Override
    public void receive(LineSplitter splitter) {
        for (int i = 0; i < itemIDs.length; i++) itemIDs[i] = splitter.getNextInt();
    }

    @Override
    public void send(LineWriter writer) {
        for (int itemID : itemIDs) writer.writeInt(itemID);
    }

}
