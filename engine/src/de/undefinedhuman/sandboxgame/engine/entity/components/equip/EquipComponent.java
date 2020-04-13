package de.undefinedhuman.sandboxgame.engine.entity.components.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

public class EquipComponent extends Component {

    public int[] itemIDs = new int[5];
    private Vector2[] itemOffsets, itemPositions;
    private String[] invisibleSprites;

    public EquipComponent(String itemLayer, String armLayer, String hitboxLayer, String[] invisibleSprites, Vector2[] itemOffsets, Vector2[] itemPositions) {
        for (int i = 0; i < itemIDs.length; i++) itemIDs[0] = -1;
        this.itemOffsets = itemOffsets;
        this.itemPositions = itemPositions;
        this.invisibleSprites = invisibleSprites;
        this.type = ComponentType.EQUIP;
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