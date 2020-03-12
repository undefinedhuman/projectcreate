package me.gentlexd.sandboxeditor.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;
import java.util.ArrayList;

public class ShoulderComponent extends Component {

    private JPanel panel;

    private StringSetting shoulderPos;
    private StringSetting shoulderOffset;

    public ShoulderComponent(JPanel panel, String name) {

        super(panel, name);
        this.panel = panel;

        shoulderPos = new StringSetting(panel,"Shoulder Pos","",false);
        shoulderOffset = new StringSetting(panel, "Shoulder Off", "",false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        shoulderPos.addToMenu(new Vector2(0,0));
        shoulderOffset.addToMenu(new Vector2(0,30));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        ArrayList<Vector2> pos = new ArrayList<>();
        String[] s = shoulderPos.getValue().toString().split(";");
        for(int i = 0; i < s.length/2; i++) pos.add(new Vector2((int) Float.parseFloat(s[i*2]), (int) Float.parseFloat(s[i*2+1])));
        writer.writeInt(pos.size());
        for(Vector2 vec : pos) writer.writeVector2(vec);

        ArrayList<Vector2> pos1 = new ArrayList<>();
        String[] s1 = shoulderOffset.getValue().toString().split(";");
        for(int i = 0; i < s1.length/2; i++) pos1.add(new Vector2((int) Float.parseFloat(s1[i*2]), (int) Float.parseFloat(s1[i*2+1])));
        writer.writeInt(pos1.size());
        for(Vector2 vec : pos1) writer.writeVector2(vec);

    }

    @Override
    public void load(FileReader reader, int id) {

        StringBuilder s = new StringBuilder();
        int size = reader.getNextInt();
        for(int i = 0; i < size; i++) s.append(reader.getNextFloat()).append(";").append(reader.getNextFloat()).append(";");
        this.shoulderPos.setValue(s.toString());

        StringBuilder s1 = new StringBuilder();
        int size1 = reader.getNextInt();
        for(int i = 0; i < size1; i++) s1.append(reader.getNextFloat()).append(";").append(reader.getNextFloat()).append(";");
        this.shoulderOffset.setValue(s1.toString());

    }

}
