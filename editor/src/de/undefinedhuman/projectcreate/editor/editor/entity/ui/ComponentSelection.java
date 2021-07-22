package de.undefinedhuman.projectcreate.editor.editor.entity.ui;

import de.undefinedhuman.projectcreate.core.ecs.ComponentType;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.editor.editor.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.editor.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

public abstract class ComponentSelection extends SelectionPanel<ComponentBlueprint> {

    private JComboBox<String> componentSelection;

    public ComponentSelection() {
        super("Components");
    }

    @Override
    public void add() {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        if(componentSelection.getSelectedItem() == null || blueprint == null)
            return;
        Class<? extends ComponentBlueprint> componentBlueprintClass = ComponentType.getComponentClass((String) componentSelection.getSelectedItem());
        if(blueprint.hasComponentBlueprints(componentBlueprintClass)) {
            Log.error("Entity already contains component type " + componentBlueprintClass.getSimpleName().split("Blueprint")[0]);
            return;
        }
        ComponentBlueprint componentBlueprint;
        try {
            componentBlueprint = componentBlueprintClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        blueprint.addComponentBlueprint(componentBlueprint);
    }

    public void remove() {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
        ComponentBlueprint selectedComponent = getSelectedID();
        if(blueprint == null || selectedComponent == null)
            return;
        blueprint.removeComponentBlueprint(selectedComponent.getClass());
        removeSelected();
    }

    @Override
    public JPanel createCreationPanel() {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));
        panel.add(componentSelection = createComponentTypeSelection(), 0.5f);
        JPanel buttonPanel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true).setFillGap(5));
        buttonPanel.add(createButton("ADD", e -> {
            add();
            updateData();
        }), 0.5f);
        buttonPanel.add(createButton("REMOVE", e -> {
            remove();
        }), 0.5f);
        panel.add(buttonPanel, 0.5f);
        return panel;
    }

    private JComboBox<String> createComponentTypeSelection() {
        JComboBox<String> componentTypeSelection = new JComboBox<>(Arrays.stream(ComponentType.getComponentTypes()).sorted().toArray(String[]::new));
        componentTypeSelection.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String type = (String) value;
                Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(getSelectedBlueprintID());
                if(blueprint != null && blueprint.hasComponentBlueprints(ComponentType.getComponentClass(type)))
                    c.setEnabled(false);
                return c;
            }
        });
        componentTypeSelection.addComponentListener(new ResizeListener(10, 0, () -> {
            if(componentTypeSelection.getItemCount() <= 0)
                return "";
            return Arrays.stream(ItemType.values()).map(Enum::name).sorted(Comparator.comparingInt(String::length)).reduce((a, b) -> b).orElse("");
        }));
        return componentTypeSelection;
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
        return blueprint.getComponentBlueprints().values().stream().sorted(Comparator.comparing(Utils::getComponentBlueprintName)).toArray(ComponentBlueprint[]::new);
    }

    @Override
    public String getTitle(ComponentBlueprint componentBlueprint) {
        return componentBlueprint.getClass().getSimpleName().split("Blueprint")[0];
    }

    public abstract int getSelectedBlueprintID();

}
