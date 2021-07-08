package de.undefinedhuman.projectcreate.engine.settings.factory;

import com.formdev.flatlaf.FlatDarculaLaf;
import de.undefinedhuman.projectcreate.engine.settings.ui.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {

    private Accordion accordion;

    public Test() {
        FlatDarculaLaf.install();
        setSize(new Dimension(1280, 720));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel canvas = new JPanel(null);
        canvas.setSize(new Dimension(1280, 720));
        accordion = new Accordion("Settings", 400, 600, Variables.BACKGROUND_COLOR.darker());
        accordion.setLocation(440, 60);
        accordion.addPanel("Float", createSettingsPanel(400));
        accordion.addPanel("Int", createSettingsPanel(400));
        accordion.addPanel("Int", createSettingsPanel(400));
        accordion.addPanel("Int", createSettingsPanel(400));
        canvas.add(accordion);
        setContentPane(canvas);
        setVisible(true);
    }

    private JPanel createSettingsPanel(int width) {
        JPanel settingsContentPanel = new JPanel(null);
        settingsContentPanel.setSize(width, 200);
        settingsContentPanel.setBackground(Color.RED);
        return settingsContentPanel;
    }

    public static void main(String[] args) {
        new Test();
    }

}
