package de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel;

import javax.swing.*;
import java.awt.*;

public abstract class AccordionPanel extends JPanel {

    public AccordionPanel(LayoutManager layout) {
        super(layout);
    }

    public abstract int getTotalHeight();

}
