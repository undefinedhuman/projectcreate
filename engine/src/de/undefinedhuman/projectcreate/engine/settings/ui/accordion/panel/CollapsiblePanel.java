package de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel;

import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.CollapsibleLabel;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CollapsiblePanel extends AccordionPanel {

    public CollapsiblePanel(String title, Color backgroundColor, JComponent content, Consumer<Boolean> visibilityChange) {
        super(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));
        add(new CollapsibleLabel(title, backgroundColor, value -> {
            content.setVisible(value);
            visibilityChange.accept(value);
        }));
        add(content);
    }

}