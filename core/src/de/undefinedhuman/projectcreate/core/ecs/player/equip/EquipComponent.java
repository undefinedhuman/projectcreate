package de.undefinedhuman.projectcreate.core.ecs.player.equip;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class EquipComponent implements Component, NetworkSerializable {

    public static final int MAX_EQUIP_ABLE_ITEMS = 1;
    public static final int DEFAULT_ID = -1;

    public int[] itemIDs = new int[MAX_EQUIP_ABLE_ITEMS];
    private Vector2[] itemOffsets, itemPositions;

    public EquipComponent(Vector2[] itemOffsets, Vector2[] itemPositions) {
        for (int i = 0; i < MAX_EQUIP_ABLE_ITEMS; i++)
            itemIDs[0] = DEFAULT_ID;
        this.itemOffsets = itemOffsets;
        this.itemPositions = itemPositions;
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

    public void setItemID(int index, int itemID) {
        if (!Utils.isInRange(index, 0, MAX_EQUIP_ABLE_ITEMS-1))
            return;
        itemIDs[index] = itemID;
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
