package de.undefinedhuman.projectcreate.editor.editor;

import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public abstract class Editor extends JPanel {

    public Editor() {
        super(new RelativeLayout().setFill(true));
        Variables.IS_EDITOR = true;
    }

    public abstract void init();

    public void save() {}
    public void load() {}

    public abstract void createMenuButtonsPanel(JPanel menuButtonPanel);

}
