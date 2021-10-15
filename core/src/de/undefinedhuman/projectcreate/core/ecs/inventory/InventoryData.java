package de.undefinedhuman.projectcreate.core.ecs.inventory;

import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.EnumSetting;

public class InventoryData extends PanelObject<String> {

    public IntSetting row = new IntSetting("Rows", 1);
    public IntSetting col = new IntSetting("Cols", 1);
    public EnumSetting<InventoryType> inventoryType = new EnumSetting<>("Type", InventoryType.values(), InventoryType::valueOf);

    public InventoryData() {
        addSettings(row, col, inventoryType);
    }

    public Inventory createInventory() {
        switch(inventoryType.getValue()) {
            case INVENTORY:
            case CRAFTING:
                return new Inventory(row.getValue(), col.getValue(), getDisplayText());
            case SELECTOR:
                return new SelectorInventory(row.getValue(), col.getValue(), getDisplayText());
        }
        return null;
    }

}
