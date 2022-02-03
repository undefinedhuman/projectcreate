package de.undefinedhuman.projectcreate.editor.types.entity;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.ecs.ComponentTypes;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameBlueprint;
import de.undefinedhuman.projectcreate.editor.types.Editor;
import de.undefinedhuman.projectcreate.editor.types.entity.ui.EntitySelectionPanel;
import de.undefinedhuman.projectcreate.editor.types.entity.ui.EntitySettingsPanel;
import de.undefinedhuman.projectcreate.editor.utils.EditorUtils;
import de.undefinedhuman.projectcreate.engine.ecs.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EntityEditor extends Editor {

    public static EntityEditor instance;

    private EntitySelectionPanel entitySelectionPanel;
    private EntitySettingsPanel entitySettingsPanel;

    public EntityEditor() {
        super();
        if(instance != null)
            return;
        instance = this;
        ComponentTypes.registerComponentTypes(BlueprintManager.getInstance());

        BlueprintManager.getInstance().loadBlueprints(
                Arrays.stream(new FsFile(Paths.ENTITY_PATH, Files.FileType.Internal).list(File::isDirectory))
                        .filter(fileHandle -> Utils.isInteger(fileHandle.name()) != null)
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
        super.init();
        entitySelectionPanel.init();
    }

    @Override
    public void createMenuButtonsPanel(JPanel menuButtonPanel) {
        menuButtonPanel.setLayout(new GridLayout(1, 3, 5, 0));
        menuButtonPanel.add(SettingsUI.createButton("Save", 0, e -> EditorUtils.saveBlueprints(entitySelectionPanel.getSelectedItems().stream().mapToInt(id -> id).toArray())));
        menuButtonPanel.add(SettingsUI.createButton("Save All", 0, e -> EditorUtils.saveBlueprints(BlueprintManager.getInstance().getBlueprintIDs().stream().mapToInt(id -> id).toArray())));
        menuButtonPanel.add(SettingsUI.createButton("Reset", 0, e -> {
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
        menuButtonPanel.add(SettingsUI.createButton("Delete", 0, e -> {
            List<Integer> removedIDs = entitySelectionPanel.getSelectedItems();
            if(removedIDs.size() == 0)
                return;
            String removeBlueprintIDsMessage = Arrays.toString(removedIDs.stream().map(id -> {
                Blueprint selectedBlueprint = BlueprintManager.getInstance().getBlueprint(id);
                NameBlueprint nameBlueprint = selectedBlueprint.getComponentBlueprint(NameBlueprint.class);
                return id + (nameBlueprint != null ? " " + nameBlueprint.name.getValue() : "");
            }).toArray(String[]::new));
            int result = JOptionPane.showConfirmDialog(null, "Delete Blueprint" + Utils.appendSToString(removedIDs.size()) + " " + removeBlueprintIDsMessage, "Are you sure you want to delete " + (removedIDs.size() > 1 ? "those" : "this") +  " blueprint" + Utils.appendSToString(removedIDs.size()) + "?", JOptionPane.YES_NO_OPTION);
            if(result != 0)
                return;
            FileUtils.deleteFile(removedIDs.stream().map(id -> new FsFile(Paths.ENTITY_PATH, id + Variables.FILE_SEPARATOR, Files.FileType.Local)).filter(FileHandle::exists).toArray(FsFile[]::new));

            BlueprintManager.getInstance().removeBlueprints(removedIDs.stream().mapToInt(value -> value).toArray());
            entitySettingsPanel.clear();
            entitySelectionPanel.removeSelected();
        }));
    }

    @Override
    public void delete() {
        super.delete();
        entitySelectionPanel.delete();
    }
}
