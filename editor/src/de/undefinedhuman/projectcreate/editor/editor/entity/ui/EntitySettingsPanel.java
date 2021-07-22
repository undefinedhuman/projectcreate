package de.undefinedhuman.projectcreate.editor.editor.entity.ui;

import de.undefinedhuman.projectcreate.editor.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class EntitySettingsPanel extends JPanel {

    private ComponentSelection componentSelection;
    private Accordion componentSettings;
    private int blueprintID = -1;

    public EntitySettingsPanel() {
        super(new RelativeLayout(RelativeLayout.X_AXIS, 40).setBorderGap(40).setFill(true));
        add(componentSelection = new ComponentSelection() {
            @Override
            public int getSelectedBlueprintID() {
                return blueprintID;
            }

            @Override
            public void select(ComponentBlueprint componentBlueprint) {
                clear();
                Utils.addSettingsGroupToAccordion(componentBlueprint, componentSettings);
            }
        }, 0.25f);
        add(componentSettings = new Accordion(Variables.BACKGROUND_COLOR), 0.5f);
        add(Box.createGlue(), 0.25f);
    }

    public void updateBlueprintID(int id) {
        if(!BlueprintManager.getInstance().hasBlueprint(id))
            return;
        this.blueprintID = id;
        clear();
        componentSelection.updateData();
    }

    public void clear() {
        componentSettings.removeAll();
    }
}
