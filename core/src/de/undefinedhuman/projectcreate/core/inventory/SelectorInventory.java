package de.undefinedhuman.projectcreate.core.inventory;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public class SelectorInventory extends Inventory {

    public int selectedIndex = 0;

    public SelectorInventory(int row, int col, String title) {
        super(row, col, title);
    }

    @Override
    public void serialize(LineWriter writer) {
        super.serialize(writer);
        writer.writeInt(selectedIndex);
    }

    @Override
    public void parse(LineSplitter splitter) {
        super.parse(splitter);
        selectedIndex = splitter.getNextInt();
    }
}
