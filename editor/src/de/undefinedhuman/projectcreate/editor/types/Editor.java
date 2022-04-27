package de.undefinedhuman.projectcreate.editor.types;

import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;

import javax.swing.*;

public abstract class Editor extends JPanel {

    public Editor() {
        setLayout(new RelativeLayout().setFill(true));
    }

    public void init() {}

    public void delete() {}

    public abstract void createMenuButtonsPanel(JPanel menuButtonPanel);

}
