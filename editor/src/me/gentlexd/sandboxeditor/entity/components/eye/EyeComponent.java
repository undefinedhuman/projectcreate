package me.gentlexd.sandboxeditor.entity.components.eye;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class EyeComponent extends Component {

    private StringSetting textureData;
    private FloatSetting scaleHeight;

    public EyeComponent(JPanel panel, String name) {

        super(panel, name);
        textureData = new StringSetting(panel,"Texture Data","",false);
        scaleHeight = new FloatSetting(panel,"Scale Height",0,false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        textureData.addToMenu(new Vector2(0,0));
        scaleHeight.addToMenu(new Vector2(0,30));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(textureData.getValue().toString());
        writer.writeFloat(Float.parseFloat(scaleHeight.getValue().toString()));

    }

    @Override
    public void load(FileReader reader, int id) {

        textureData.setValue(reader.getNextString());
        scaleHeight.setValue(reader.getNextFloat());

    }

}
