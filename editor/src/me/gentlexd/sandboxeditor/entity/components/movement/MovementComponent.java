package me.gentlexd.sandboxeditor.entity.components.movement;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class MovementComponent extends Component {

    private JPanel panel;
    private JLabel label;

    private FloatSetting speed, jumpSpeed, gravity;

    public MovementComponent(JPanel panel, String name) {

        super(panel, name);
        this.panel = panel;

        label = new JLabel("Animation Modes: LOOP, NORMAL");

        speed = new FloatSetting(panel,"Speed",0, false);
        jumpSpeed = new FloatSetting(panel,"Jump-Speed",0, false);
        gravity = new FloatSetting(panel,"Gravity",0, false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        label.setBounds(0,0,300,25);
        panel.add(label);

        speed.addToMenu(new Vector2(0,30));
        jumpSpeed.addToMenu(new Vector2(0,60));
        gravity.addToMenu(new Vector2(0,90));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeFloat(Float.parseFloat(speed.getValue().toString()));
        writer.writeFloat(Float.parseFloat(jumpSpeed.getValue().toString()));
        writer.writeFloat(Float.parseFloat(gravity.getValue().toString()));

    }

    @Override
    public void load(FileReader reader, int id) {

        speed.setValue(reader.getNextFloat());
        jumpSpeed.setValue(reader.getNextFloat());
        gravity.setValue(reader.getNextFloat());

    }

}
