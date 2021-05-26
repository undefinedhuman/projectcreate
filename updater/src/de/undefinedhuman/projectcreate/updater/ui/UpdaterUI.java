package de.undefinedhuman.projectcreate.updater.ui;

import de.undefinedhuman.projectcreate.engine.resources.RescourceUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;
import java.awt.*;

public class UpdaterUI extends JPanel {

    private static final Vector2i LOGO_SIZE = new Vector2i(288, 96);
    private static final Dimension WINDOW_SIZE = new Dimension(LOGO_SIZE.x, LOGO_SIZE.y + 40);

    private JLabel progressText;

    public UpdaterUI() {
        setLayout(null);
        setBackground(Variables.BACKGROUND_COLOR);
        setSize(WINDOW_SIZE);
        add(createLogoLabel());
        add(createProgressBarUI());
        add(createProgressTextUI());
    }

    public void updateProgressText(String updateMessage) {
        progressText.setText(updateMessage);
    }

    public Dimension getWindowSize() {
        return WINDOW_SIZE;
    }

    private JLabel createLogoLabel() {
        JLabel logo = new JLabel(new ImageIcon(RescourceUtils.loadImage("logo/" + UpdaterUI.LOGO_SIZE.x + "x" + UpdaterUI.LOGO_SIZE.y + ".png")));
        logo.setBounds(0, 0, UpdaterUI.LOGO_SIZE.x, UpdaterUI.LOGO_SIZE.y);
        return logo;
    }

    private JProgressBar createProgressBarUI() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(0, LOGO_SIZE.y + 5, LOGO_SIZE.x, 10);
        progressBar.setIndeterminate(true);
        return progressBar;
    }

    private JLabel createProgressTextUI() {
        progressText = new JLabel("");
        progressText.setBounds(0, LOGO_SIZE.y + 20, LOGO_SIZE.x, 15);
        progressText.setHorizontalAlignment(SwingConstants.CENTER);
        return progressText;
    }

}
