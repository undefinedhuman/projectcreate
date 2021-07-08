package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;

import javax.swing.*;
import java.awt.*;

public class OverviewPanel extends JPanel {

    private JLabel logo;

    public OverviewPanel() {
        setBackground(Color.BLUE);
        setBounds(0, 0, 1280/4, 620);
        setLayout(null);
        initLabel();
    }

    public void initLabel() {
        JLabel label = new JLabel(new ImageIcon(RessourceUtils.loadImage("logo/288x96.png")));
        label.setSize(new Dimension(288, 96));
        add(label);
    }

}
