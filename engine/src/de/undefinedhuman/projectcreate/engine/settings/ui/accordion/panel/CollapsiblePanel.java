package de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel;

import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.CollapsibleLabel;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CollapsiblePanel extends AccordionPanel {

    private int height;
    private JComponent content;

    public CollapsiblePanel(String title, Color backgroundColor, JComponent content, Consumer<Boolean> visibilityChange) {
        super(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));
        this.content = content;
        this.height = calculateHeight(true);
        setSize(0, height);
        add(new CollapsibleLabel(title, backgroundColor, value -> {
            content.setVisible(value);
            height = calculateHeight(value);
            setSize(getWidth(), height);
            visibilityChange.accept(value);
        }));
        add(content);
    }

    private int calculateHeight(boolean visible) {
        int height = Variables.DEFAULT_CONTENT_HEIGHT;
        if(visible)
            height += content.getHeight();
        return height;
    }

    @Override
    public int getTotalHeight() {
        return height;
    }

}