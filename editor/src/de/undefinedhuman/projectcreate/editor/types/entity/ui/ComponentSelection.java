package de.undefinedhuman.projectcreate.editor.types.entity.ui;

import de.undefinedhuman.projectcreate.editor.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.engine.ecs.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ComponentSelection extends SelectionPanel<ComponentBlueprint> {

    private DefaultListModel<String> componentListModel;
    private JList<String> componentSelection;

    public ComponentSelection() {
        super("Components", 4.15f);
    }

    @Override
    public void init() {
        super.init();
        componentSelection.setSelectedIndex(0);
    }

    @Override
    public void add() {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        int[] selectedIndices = componentSelection.getSelectedIndices();
        if(selectedIndices.length == 0 || blueprint == null)
            return;
        for(int i = selectedIndices.length-1; i >= 0; i--) {
            ComponentBlueprint componentBlueprint = BlueprintManager.getInstance().getComponentBlueprint(componentListModel.getElementAt(selectedIndices[i]), blueprint.getBlueprintID());
            if(componentBlueprint == null || blueprint.hasComponentBlueprints(componentBlueprint.getClass()))
                continue;
            blueprint.addComponentBlueprints(componentBlueprint);
        }
    }

    public void remove() {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        if(blueprint == null)
            return;
        List<ComponentBlueprint> removedComponents = removeSelected();
        for(ComponentBlueprint removedComponent : removedComponents)
            blueprint.removeComponentBlueprints(removedComponent.getClass());
    }

    @Override
    public void createMenuPanels(JPanel parentPanel) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(componentSelection = createList(BlueprintManager.getInstance().getComponentBlueprintClassKeys().stream().sorted().toArray(String[]::new), s -> {}, JLabel::setText));
        componentListModel = (DefaultListModel<String>) componentSelection.getModel();

        JPanel buttonPanel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS, 5).setFill(true));
        buttonPanel.add(SettingsUI.createButton("REMOVE", 0, e -> {
            remove();
            updateData();
        }), 0.5f);
        buttonPanel.add(SettingsUI.createButton("ADD", 0, e -> {
            add();
            updateData();
        }), 0.5f);

        parentPanel.add(buttonPanel, 0.8f);
        parentPanel.add(scrollPane, 4.2f
        );
    }

    @Override
    public ComponentBlueprint[] getListData() {
        if(!RessourceUtils.existBlueprint(getSelectedBlueprintID()))
            return new ComponentBlueprint[0];
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        if(blueprint == null)
            return new ComponentBlueprint[0];
        return blueprint.getComponentBlueprints().values().stream().sorted().toArray(ComponentBlueprint[]::new);
    }

    @Override
    public void updateData() {
        super.updateData();
        int selectedIndex = componentSelection.getSelectedIndex();
        componentListModel.removeAllElements();
        List<String> currentComponents = Arrays.stream(currentData).map(ComponentBlueprint::toString).collect(Collectors.toList());
        BlueprintManager.getInstance().getComponentBlueprintClassKeys().stream().filter(name -> !currentComponents.contains(name)).sorted().forEach(componentListModel::addElement);
        componentSelection.setSelectedIndex(selectedIndex >= componentListModel.size() ? 0 : selectedIndex);
    }

    @Override
    public String getTitle(ComponentBlueprint componentBlueprint) {
        return componentBlueprint.toString();
    }

    public abstract int getSelectedBlueprintID();

    @Override
    public void renderCell(JLabel label, ComponentBlueprint componentBlueprint) {
        label.setText(getTitle(componentBlueprint));
    }
}
