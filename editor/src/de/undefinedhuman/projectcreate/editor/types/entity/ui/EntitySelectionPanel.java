package de.undefinedhuman.projectcreate.editor.types.entity.ui;

import de.undefinedhuman.projectcreate.core.ecs.name.NameBlueprint;
import de.undefinedhuman.projectcreate.editor.types.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class EntitySelectionPanel extends SelectionPanel<Integer> {

    public EntitySelectionPanel() {
        super("Entities", key -> {
            if(key.getKey1().matches("^[0-9]+-[0-9]+$")) {
                String[] ids = key.getKey1().split("-");
                Integer lower = Tools.isInteger(ids[0]), upper = Tools.isInteger(ids[1]);
                if(lower != null && upper != null && lower <= upper)
                    return Arrays.stream(key.getKey2()).filter(itemID -> Tools.isInRange(itemID, lower, upper)).toArray(Integer[]::new);
            }
            return new Integer[0];
        });
    }

    @Override
    public void add() {
        Integer[] ids = BlueprintManager.getInstance().getBlueprintIDs().toArray(new Integer[0]);
        int newID = Utils.findSmallestMissing(ids, 0, ids.length-1);
        Blueprint blueprint = new Blueprint();
        BlueprintManager.getInstance().addBlueprint(newID, blueprint);
        Utils.saveBlueprints(newID);
    }

    @Override
    public void createMenuPanels(JPanel parentPanel) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true).setFillGap(10));
        panel.add(createNewButton(), 1f);
        parentPanel.add(panel, 0.5f);
    }

    private JButton createNewButton() {
        JButton button = new JButton("CREATE");
        button.addComponentListener(new ResizeListener(15, 0, button::getText));
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
        NameBlueprint nameBlueprint = (NameBlueprint) blueprint.getComponentBlueprint(NameBlueprint.class);
        return id + (nameBlueprint != null ?  " " + nameBlueprint.name.getValue() : "");
    }

}
