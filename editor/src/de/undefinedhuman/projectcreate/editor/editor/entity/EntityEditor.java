package de.undefinedhuman.projectcreate.editor.editor.entity;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.core.ecs.name.NameBlueprint;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
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
        menuButtonPanel.add(createUtilityButton("Save", e -> {
            int selectedID = entitySelectionPanel.getSelectedID();
            if(selectedID == -1)
                return;
            Utils.saveBlueprint(selectedID);
        }));
        menuButtonPanel.add(createUtilityButton("Reset", e -> {
            int selectedID = entitySelectionPanel.getSelectedID();
            if(selectedID == -1)
                return;
            BlueprintManager.getInstance().removeBlueprints(selectedID);
            entitySettingsPanel.clear();
            BlueprintManager.getInstance().loadBlueprints(selectedID);
            entitySelectionPanel.select(selectedID);

        }));
        menuButtonPanel.add(createUtilityButton("Delete", e -> {
            int selectedID = entitySelectionPanel.getSelectedID();
            if(selectedID == -1)
                return;
            Blueprint selectedBlueprint = BlueprintManager.getInstance().getBlueprint(selectedID);
            NameBlueprint nameBlueprint = (NameBlueprint) selectedBlueprint.getComponentBlueprint(NameBlueprint.class);
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this blueprint?", "Delete Blueprint " + selectedID + " " + (nameBlueprint != null ? nameBlueprint.name.getValue() : ""), JOptionPane.YES_NO_OPTION);
            if(result != 0)
                return;
            FsFile blueprintDir = new FsFile(Paths.ENTITY_PATH, selectedID + Variables.FILE_SEPARATOR, Files.FileType.Local);
            if(blueprintDir.exists())
                FileUtils.deleteFile(blueprintDir);
            removeItemFromUI(selectedID);
        }));
    }

    private JButton createUtilityButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        return button;
    }

    private void removeItemFromUI(int id) {
        ItemManager.getInstance().removeItems(id);
        entitySelectionPanel.removeSelected();
        entitySettingsPanel.clear();
    }
}
