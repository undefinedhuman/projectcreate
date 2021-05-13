package de.undefinedhuman.projectcreate.core.ecs.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class EquipComponent extends Component {

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
        return itemOffsets[index];
    }

    public String[] getInvisibleSprites() { return invisibleSprites; }

    public void setItemID(int index, int itemID) {
        if (!Tools.isInRange(index, 0, 4)) return;
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
