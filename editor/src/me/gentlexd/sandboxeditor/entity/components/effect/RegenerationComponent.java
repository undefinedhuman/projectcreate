package me.gentlexd.sandboxeditor.entity.components.effect;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.IntSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class RegenerationComponent extends Component {

    private FloatSetting delay;
    private IntSetting regAmount;

    public RegenerationComponent(JPanel panel, String name) {

        super(panel, name);
        delay = new FloatSetting(panel,"Reg Delay",0, false);
        regAmount = new IntSetting(panel,"Reg. Amount",0, false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        delay.addToMenu(new Vector2(0,0));
        regAmount.addToMenu(new Vector2(0,30));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeFloat(Float.parseFloat(delay.getValue().toString()));
        writer.writeInt(Integer.parseInt(regAmount.getValue().toString()));

    }

    @Override
    public void load(FileReader reader, int id) {

        delay.setValue(reader.getNextFloat());
        regAmount.setValue(reader.getNextInt());

    }

}
