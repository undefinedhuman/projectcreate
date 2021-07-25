package de.undefinedhuman.projectcreate.editor.types.entity.ui;

import de.undefinedhuman.projectcreate.core.ecs.ComponentType;
import de.undefinedhuman.projectcreate.editor.types.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
            Class<? extends ComponentBlueprint> componentBlueprintClass = ComponentType.getComponentClass(componentListModel.getElementAt(selectedIndices[i]));
            if(blueprint.hasComponentBlueprints(componentBlueprintClass))
                continue;
            ComponentBlueprint componentBlueprint;
            try {
                componentBlueprint = componentBlueprintClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
            blueprint.addComponentBlueprint(componentBlueprint);
        }
    }

    public void remove() {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        if(blueprint == null)
            return;
        List<ComponentBlueprint> removedComponents = removeSelected();
        for(ComponentBlueprint removedComponent : removedComponents)
            blueprint.removeComponentBlueprint(removedComponent.getClass());
    }

    @Override
    public void createMenuPanels(JPanel parentPanel) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(componentSelection = createList(Arrays.stream(ComponentType.getComponentTypes()).sorted().toArray(String[]::new), s -> {}, s -> s));
        componentListModel = (DefaultListModel<String>) componentSelection.getModel();

        JPanel buttonPanel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS, 5).setFill(true));
        buttonPanel.add(createButton("REMOVE", e -> {
            remove();
            updateData();
        }), 0.5f);
        buttonPanel.add(createButton("ADD", e -> {
            add();
            updateData();
        }), 0.5f);

        parentPanel.add(buttonPanel, 1f);
        parentPanel.add(scrollPane, 4f);
    }

    private JButton createButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.addComponentListener(new ResizeListener(10, 0, button::getText));
        button.setFont(button.getFont().deriveFont(25f).deriveFont(Font.BOLD));
        button.addActionListener(actionListener);
        return button;
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
        Arrays.stream(ComponentType.getComponentTypes()).filter(name -> !currentComponents.contains(name)).sorted().forEach(componentListModel::addElement);
        componentSelection.setSelectedIndex(selectedIndex >= componentListModel.size() ? 0 : selectedIndex);
    }

    @Override
    public String getTitle(ComponentBlueprint componentBlueprint) {
        return componentBlueprint.toString();
    }

    public abstract int getSelectedBlueprintID();

}
