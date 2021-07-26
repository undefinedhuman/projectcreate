package de.undefinedhuman.projectcreate.editor.types;

import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public abstract class Editor extends JPanel {

    public Editor() {
        super(new RelativeLayout().setFill(true));
        Variables.IS_EDITOR = true;
    }

    public void init() {}

    public abstract void createMenuButtonsPanel(JPanel menuButtonPanel);

}
