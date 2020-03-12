package me.gentlexd.sandboxeditor.entity.components.health;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class HealthComponent extends Component {

    private FloatSetting maxHealth;

    public HealthComponent(JPanel panel, String name) {

        super(panel, name);
        maxHealth = new FloatSetting(panel,"Max Health",0,false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        maxHealth.addToMenu(new Vector2(0,0));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeFloat(Float.parseFloat(maxHealth.getValue().toString()));

    }

    @Override
    public void load(FileReader reader, int id) {

        maxHealth.setValue(reader.getNextFloat());

    }

}
