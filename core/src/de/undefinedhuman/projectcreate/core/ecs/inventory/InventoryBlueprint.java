package de.undefinedhuman.projectcreate.core.ecs.inventory;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.panels.StringPanel;

public class InventoryBlueprint extends ComponentBlueprint {

    private StringPanel<InventoryData> inventories = new StringPanel<>("Inventories", InventoryData.class);

    public InventoryBlueprint() {
        addSettings(inventories);
        priority = ComponentPriority.HIGH;
    }

    @Override
    public Component createInstance() {
        return new InventoryComponent(inventories.getValue());
    }
}
