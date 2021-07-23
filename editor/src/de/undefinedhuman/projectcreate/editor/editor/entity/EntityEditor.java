package de.undefinedhuman.projectcreate.editor.editor.entity;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.ecs.name.NameBlueprint;
import de.undefinedhuman.projectcreate.editor.Window;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.editor.editor.entity.ui.EntitySelectionPanel;
import de.undefinedhuman.projectcreate.editor.editor.entity.ui.EntitySettingsPanel;
import de.undefinedhuman.projectcreate.editor.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EntityEditor extends Editor {

    private EntitySelectionPanel entitySelectionPanel;
    private EntitySettingsPanel entitySettingsPanel;

    public EntityEditor() {
        super();

        BlueprintManager.getInstance().loadBlueprints(
                Arrays.stream(new FsFile(Paths.ENTITY_PATH, Files.FileType.Internal).list(File::isDirectory))
                        .filter(fileHandle -> Tools.isInteger(fileHandle.name()) != null)
                        .mapToInt(fileHandle -> Integer.parseInt(fileHandle.name())).toArray()
        );

        add(entitySelectionPanel = new EntitySelectionPanel() {
            @Override
            public void select(Integer id) {
                entitySettingsPanel.updateBlueprintID(id);
            }
        }, 0.15f);

        add(entitySettingsPanel = new EntitySettingsPanel(), 0.85f);
    }

    @Override
    public void init() {
        entitySelectionPanel.init();
    }

    @Override
    public void createMenuButtonsPanel(JPanel menuButtonPanel) {
        menuButtonPanel.setLayout(new GridLayout(1, 3, 5, 0));
        menuButtonPanel.setMinimumSize(new Dimension(100, Window.MENU_HEIGHT));
        menuButtonPanel.add(createUtilityButton("Save", e -> entitySelectionPanel.getSelectedItems().forEach(Utils::saveBlueprint)));
        menuButtonPanel.add(createUtilityButton("Reset", e -> {
            List<Integer> selectedIDs = entitySelectionPanel.getSelectedItems();
            if(selectedIDs.size() == 0)
                return;
            entitySettingsPanel.clear();
            entitySelectionPanel.getSelectedItems().forEach(id -> {
                BlueprintManager.getInstance().removeBlueprints(id);
                BlueprintManager.getInstance().loadBlueprints(id);
            });
            entitySelectionPanel.select(entitySelectionPanel.getSelectedIndex());
        }));
        menuButtonPanel.add(createUtilityButton("Delete", e -> {
            List<Integer> removedIDs = entitySelectionPanel.getSelectedItems();
            if(removedIDs.size() == 0)
                return;
            String removeBlueprintIDsMessage = Arrays.toString(removedIDs.stream().map(id -> {
                Blueprint selectedBlueprint = BlueprintManager.getInstance().getBlueprint(id);
                NameBlueprint nameBlueprint = (NameBlueprint) selectedBlueprint.getComponentBlueprint(NameBlueprint.class);
                return id + (nameBlueprint != null ? " " + nameBlueprint.name.getValue() : "");
            }).toArray(String[]::new));
            int result = JOptionPane.showConfirmDialog(null, "Delete Blueprint" + Tools.appendSToString(removedIDs.size()) + " " + removeBlueprintIDsMessage, "Are you sure you want to delete " + (removedIDs.size() > 1 ? "those" : "this") +  " blueprint" + Tools.appendSToString(removedIDs.size()) + "?", JOptionPane.YES_NO_OPTION);
            if(result != 0)
                return;
            FileUtils.deleteFile(removedIDs.stream().map(id -> new FsFile(Paths.ENTITY_PATH, id + Variables.FILE_SEPARATOR, Files.FileType.Local)).filter(FileHandle::exists).toArray(FsFile[]::new));

            BlueprintManager.getInstance().removeBlueprints(removedIDs.stream().mapToInt(value -> value).toArray());
            entitySettingsPanel.clear();
            entitySelectionPanel.removeSelected();
        }));
    }

    private JButton createUtilityButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        return button;
    }

}
