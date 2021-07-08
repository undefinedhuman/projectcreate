package de.undefinedhuman.projectcreate.editor.editor;

import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public abstract class Editor extends JPanel {

    protected static final int EDITOR_PANEL_HEIGHT = 1025;

    public Editor() {
        super(new GridBagLayout());
        Variables.IS_EDITOR = true;
    }

    public abstract void save();
    public abstract void load();

    public abstract void createMenuButtonsPanel(JPanel menuButtonPanel);

}
