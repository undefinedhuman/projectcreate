package me.gentlexd.sandboxeditor.entity.components.collision;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class CollisionComponent extends Component {

    private JPanel panel;

    private FloatSetting width, height;
    private Vector2Setting offset;

    public CollisionComponent(JPanel panel, String name) {

        super(panel, name);
        this.panel = panel;

        width = new FloatSetting(panel,"Width",0, false);
        height = new FloatSetting(panel,"Height",0, false);
        offset = new Vector2Setting(panel,"Offset", new Vector2(0,0), false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        width.addToMenu(new Vector2(0,0));
        height.addToMenu(new Vector2(0,30));
        offset.addToMenu(new Vector2(0,60));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeFloat(Float.parseFloat(width.getValue().toString()));
        writer.writeFloat(Float.parseFloat(height.getValue().toString()));
        writer.writeVector2(offset.getVector());

    }

    @Override
    public void load(FileReader reader, int id) {

        width.setValue(reader.getNextFloat());
        height.setValue(reader.getNextFloat());
        offset.setValue(reader.getNextVector2());

    }

}
