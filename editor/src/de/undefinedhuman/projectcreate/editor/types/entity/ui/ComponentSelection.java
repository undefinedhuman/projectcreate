package de.undefinedhuman.projectcreate.editor.types.entity.ui;

import de.undefinedhuman.projectcreate.editor.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.engine.ecs.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;

import javax.swing.*;
import java.util.List;

public abstract class ComponentSelection extends SelectionPanel<Class<? extends ComponentBlueprint>> {

    private DefaultListModel<Class<? extends ComponentBlueprint>> componentListModel;
    private JList<Class<? extends ComponentBlueprint>> componentSelection;

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
        for(int i = selectedIndices.length-1; i >= 0; i--)
            addComponentBlueprint(blueprint, componentListModel.getElementAt(selectedIndices[i]));
    }

    private void addComponentBlueprint(Blueprint blueprint, Class<? extends ComponentBlueprint> componentBlueprintType) {
        if(blueprint.hasComponentBlueprints(componentBlueprintType)) return;
        ComponentBlueprint componentBlueprint = BlueprintManager.getInstance().createComponentBlueprint(componentBlueprintType, blueprint.getBlueprintID());
        if(componentBlueprint == null) return;
        blueprint.addComponentBlueprints(componentBlueprint);
        if(componentBlueprint.getRequiredComponents() != null)
            for (Class<? extends ComponentBlueprint> requiredComponent : componentBlueprint.getRequiredComponents())
                addComponentBlueprint(blueprint, requiredComponent);
    }

    public void remove() {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        if(blueprint == null)
            return;
        List<Class<? extends ComponentBlueprint>> removedComponents = removeSelected();
        for(Class<? extends ComponentBlueprint> removedComponent : removedComponents)
            blueprint.removeComponentBlueprints(removedComponent);
    }

    @Override
    public void createMenuPanels(JPanel parentPanel) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(
                componentSelection = createList(
                        BlueprintManager.getInstance().getComponentBlueprintClasses()
                                .stream()
                                .map(ComponentBlueprint::getName)
                                .sorted()
                                .map(name -> BlueprintManager.getInstance().getComponentBlueprintClass(name))
                                .toArray(Class[]::new),
                        s -> {}, (label, componentBlueprintClass) -> label.setText(ComponentBlueprint.getName(componentBlueprintClass))));
        componentListModel = (DefaultListModel<Class<? extends ComponentBlueprint>>) componentSelection.getModel();

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
    public Class<? extends ComponentBlueprint>[] getListData() {
        if(!RessourceUtils.existBlueprint(getSelectedBlueprintID()))
            return new Class[0];
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        if(blueprint == null)
            return new Class[0];
        return blueprint
                .getComponentBlueprints()
                .keySet()
                .stream()
                .map(ComponentBlueprint::getName)
                .sorted()
                .map(name -> BlueprintManager.getInstance().getComponentBlueprintClass(name))
                .toArray(Class[]::new);
    }

    @Override
    public void updateData() {
        super.updateData();
        int selectedIndex = componentSelection.getSelectedIndex();
        componentListModel.removeAllElements();
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        BlueprintManager.getInstance().getComponentBlueprintClasses().stream()
                .filter(componentBlueprintClass -> {
                    if(blueprint == null) return true;
                    return !blueprint.hasComponentBlueprints(componentBlueprintClass);
                })
                .map(ComponentBlueprint::getName)
                .sorted()
                .map(name -> BlueprintManager.getInstance().getComponentBlueprintClass(name))
                .forEach(componentListModel::addElement);
        componentSelection.setSelectedIndex(selectedIndex >= componentListModel.size() ? 0 : selectedIndex);
    }

    @Override
    public String getTitle(Class<? extends ComponentBlueprint> componentBlueprint) {
        return ComponentBlueprint.getName(componentBlueprint);
    }

    public abstract int getSelectedBlueprintID();

    @Override
    public void renderCell(JLabel label, Class<? extends ComponentBlueprint> componentBlueprint) {
        label.setText(getTitle(componentBlueprint));
    }
}
