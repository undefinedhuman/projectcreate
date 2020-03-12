package me.gentlexd.sandboxeditor.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.engine.file.Paths;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Editor {

    Paths path;

    JPanel mainPanel, settingsPanel;
    public ArrayList<Setting> settings;

    Editor(Container container) {

        settings = new ArrayList<>();

        mainPanel = new JPanel(null, true);
        mainPanel.setBounds(25,25,480,260);
        mainPanel.setBorder(BorderFactory.createTitledBorder("Main:"));

        settingsPanel = new JPanel(null, true);
        settingsPanel.setPreferredSize(new Dimension( 400,580));

        JScrollPane scrollFrame = new JScrollPane(settingsPanel);
        scrollFrame.setBounds(780,25,410,620);
        scrollFrame.setBorder(BorderFactory.createTitledBorder("Settings:"));
        settingsPanel.setAutoscrolls(true);

        container.add(scrollFrame);
        container.add(mainPanel);

        init();
        int i = 0, i2 = 0;
        for(Setting setting : settings) {

            if(setting.getPanel().equals(mainPanel)) {

                setting.setPosition(new Vector2(40, 20 + 30 * i));
                i++;

            } else {

                setting.setPosition(new Vector2(40, 20 + 30 * i2));
                i2++;

            }

        }

    }

    public Paths getPath() {
        return path;
    }

    public abstract void init();
    public abstract void render(SpriteBatch batch);

}
