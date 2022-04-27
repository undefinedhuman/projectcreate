package de.undefinedhuman.projectcreate.editor.types.entity.ui;

import de.undefinedhuman.projectcreate.editor.utils.EditorUtils;
import de.undefinedhuman.projectcreate.engine.ecs.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.util.Map;

public class EntitySettingsPanel extends JPanel {

    private ComponentSelection componentSelection;
    private Accordion componentSettings, selectedComponentSettings;
    private int blueprintID = -1;

    public EntitySettingsPanel() {
        super(new RelativeLayout(RelativeLayout.X_AXIS, 40).setBorderGap(40).setFill(true));
        add(componentSelection = new ComponentSelection() {
            @Override
            public int getSelectedBlueprintID() {
                return blueprintID;
            }

            @Override
            public void select(Class<? extends ComponentBlueprint> selectedComponentBlueprint) {
                clear();
                Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(blueprintID);
                if(selectedComponentBlueprint == null || blueprint == null)
                    return;

                EditorUtils.addComponentBlueprintsToAccordion(componentSettings, blueprint
                        .getComponentBlueprints()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getKey() != selectedComponentBlueprint)
                        .map(Map.Entry::getValue)
                        .filter(componentBlueprint -> !componentBlueprint.isEmpty())
                        .sorted()
                        .toArray(ComponentBlueprint[]::new));
                EditorUtils.addComponentBlueprintsToAccordion(selectedComponentSettings, blueprint.getComponentBlueprint(selectedComponentBlueprint));
            }
        }, 0.2f);
        add(componentSettings = new Accordion(Variables.BACKGROUND_COLOR), 0.25f);
        add(selectedComponentSettings = new Accordion(Variables.BACKGROUND_COLOR), 0.25f);
        add(Box.createGlue(), 0.3f);
    }

    public void updateBlueprintID(int id) {
        if(!BlueprintManager.getInstance().hasBlueprint(id))
            return;
        this.blueprintID = id;
        clear();
        componentSelection.updateData();
        componentSelection.init();
    }

    public void clear() {
        componentSettings.removeAll();
        selectedComponentSettings.removeAll();
    }

}
