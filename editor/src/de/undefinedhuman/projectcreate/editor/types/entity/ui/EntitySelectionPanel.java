package de.undefinedhuman.projectcreate.editor.types.entity.ui;

import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameBlueprint;
import de.undefinedhuman.projectcreate.editor.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.editor.utils.EditorUtils;
import de.undefinedhuman.projectcreate.engine.ecs.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.observer.Observer;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

public abstract class EntitySelectionPanel extends SelectionPanel<Integer> {

    private Observer<ComponentBlueprint[]> nameBlueprintObserver = componentBlueprints -> {
        Optional<ComponentBlueprint> nameBlueprint = Arrays.stream(componentBlueprints).filter(componentBlueprint -> componentBlueprint instanceof NameBlueprint).findAny();
        if(nameBlueprint.isPresent())
            update();
    };

    public EntitySelectionPanel() {
        super("Entities", key -> {
            if(key.getT().matches("^[0-9]+-[0-9]+$")) {
                String[] ids = key.getT().split("-");
                Integer lower = Utils.isInteger(ids[0]), upper = Utils.isInteger(ids[1]);
                if(lower != null && upper != null && lower <= upper)
                    return Arrays.stream(key.getU()).filter(itemID -> Utils.isInRange(itemID, lower, upper)).toArray(Integer[]::new);
            }
            return new Integer[0];
        });
    }

    @Override
    public void init() {
        super.init();
        BlueprintManager.getInstance().subscribe(Blueprint.ComponentBlueprintEvent.class, Blueprint.ComponentBlueprintEvent.Type.UPDATE, nameBlueprintObserver);
    }

    @Override
    public void delete() {
        super.delete();
        BlueprintManager.getInstance().unsubscribe(Blueprint.ComponentBlueprintEvent.class, Blueprint.ComponentBlueprintEvent.Type.UPDATE, nameBlueprintObserver);
    }

    @Override
    public void add() {
        Integer[] ids = BlueprintManager.getInstance().getBlueprintIDs().toArray(new Integer[0]);
        int newID = EditorUtils.findSmallestMissing(ids, 0, ids.length-1);
        Blueprint blueprint = new Blueprint(newID);
        BlueprintManager.getInstance().addBlueprints(blueprint);
        EditorUtils.saveBlueprints(newID);
    }

    @Override
    public void createMenuPanels(JPanel parentPanel) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true).setFillGap(10));
        panel.add(createNewButton(), 1f);
        parentPanel.add(panel, 0.5f);
    }

    private JButton createNewButton() {
        JButton button = new JButton("CREATE");
        button.addComponentListener(new ResizeListener(() -> new Component[] {button}, 15, button::getText));
        button.setFont(button.getFont().deriveFont(25f).deriveFont(Font.BOLD));
        button.addActionListener(e -> {
            add();
            updateData();
        });
        return button;
    }

    @Override
    public Integer[] getListData() {
        return BlueprintManager
                .getInstance()
                .getBlueprintIDs()
                .toArray(new Integer[0]);
    }

    @Override
    public String getTitle(Integer id) {
        if(!BlueprintManager.getInstance().hasBlueprint(id))
            return "ERROR " + " TITLE NOT FOUND, ID: " + id;
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(id);
        NameBlueprint nameBlueprint = blueprint.getComponentBlueprint(NameBlueprint.class);
        return id + (nameBlueprint != null ?  " " + nameBlueprint.name.getValue() : "");
    }

    @Override
    public void renderCell(JLabel label, Integer integer) {
        label.setText(getTitle(integer));
    }
}
