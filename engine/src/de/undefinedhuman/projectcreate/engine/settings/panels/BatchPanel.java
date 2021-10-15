package de.undefinedhuman.projectcreate.engine.settings.panels;

import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;

import javax.swing.*;

public abstract class BatchPanel<T extends PanelObject<String>> extends StringPanel<T> {

    private static final float BUTTON_WIDTH = 0.6f;

    public BatchPanel(String name, Class<T> panelObjectClass) {
        super(name, panelObjectClass);
    }

    @Override
    protected void createUtilityButtons(JPanel panel, float remainingWidth) {
        panel.add(SettingsUI.createButton("Aseprite", BUTTON_HEIGHT, e -> AsepriteUtils.loadAsepriteFile(this::loadBatch)), BUTTON_WIDTH);
        super.createUtilityButtons(panel, remainingWidth - BUTTON_WIDTH);
    }

    public abstract void loadBatch(FsFile file);

}
