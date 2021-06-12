package de.undefinedhuman.projectcreate.editor.editor;

import javax.swing.*;
import java.awt.*;

public abstract class ThreePanelEditor extends Editor {

    protected JPanel leftPanel, middlePanel, rightPanel;

    public ThreePanelEditor(Container container, int width, int height) {
        super(container, width, height);
        addPanel(
                container,
                createScrollPane(leftPanel = createJPanel(820, 990), "Main:", 20, 15, 820, 990, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                createScrollPane(middlePanel = createJPanel(200, 990), "Type:", 860, 15, 200, 990, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                createScrollPane(rightPanel = createJPanel(820, 990), "Settings:", 1080, 15, 820, 990, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        );
    }
}
