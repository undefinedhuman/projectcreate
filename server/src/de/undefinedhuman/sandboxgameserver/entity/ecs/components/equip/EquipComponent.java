package de.undefinedhuman.sandboxgameserver.entity.ecs.components.equip;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class EquipComponent extends Component {

    public int[] itemIDs = new int[5];

    public EquipComponent(Entity entity) {

        super(entity);
        this.type = ComponentType.EQUIP;

    }

    @Override
    public void load(FileReader reader) {
        for(int i = 0; i < itemIDs.length; i++) itemIDs[i] = reader.getNextInt();
    }

    @Override
    public void save(FileWriter writer) {
        for (int itemID : itemIDs) writer.writeInt(itemID);
    }

    @Override
    public void getNetworkData(LineWriter w) {
        for (int itemID : itemIDs) w.writeInt(itemID);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        for(int i = 0; i < itemIDs.length; i++) itemIDs[i] = s.getNextInt();
    }

}
