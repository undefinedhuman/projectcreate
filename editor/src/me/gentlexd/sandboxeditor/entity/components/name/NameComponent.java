package me.gentlexd.sandboxeditor.entity.components.name;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class NameComponent extends Component {

    private StringSetting nameSetting;

    public NameComponent(JPanel panel, String name) {

        super(panel, name);
        nameSetting = new StringSetting(panel,"Name","",false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        nameSetting.addToMenu(new Vector2(0,0));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(nameSetting.getValue().toString());

    }

    @Override
    public void load(FileReader reader, int id) {

        nameSetting.setValue(reader.getNextString());

    }

}
