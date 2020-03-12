package me.gentlexd.sandboxeditor.entity.components.equip;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;
import java.util.ArrayList;

public class EquipComponent extends Component {

    private StringSetting itemTexture, itemHitboxTexture, rightArmTexture, itemOffset;

    public EquipComponent(JPanel panel, String name) {

        super(panel, name);

        itemTexture = new StringSetting(panel,"Item Texture","Temp",false);
        itemHitboxTexture = new StringSetting(panel,"Item Hitbox","Temp",false);
        rightArmTexture = new StringSetting(panel,"Right Arm","Temp",false);
        itemOffset = new StringSetting(panel, "Item A. Offset","",false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        itemTexture.addToMenu(new Vector2(0,0));
        itemHitboxTexture.addToMenu(new Vector2(0,30));
        rightArmTexture.addToMenu(new Vector2(0,60));
        itemOffset.addToMenu(new Vector2(0,90));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(itemTexture.getValue().toString());
        writer.writeString(itemHitboxTexture.getValue().toString());
        writer.writeString(rightArmTexture.getValue().toString());

        ArrayList<Vector2> vecs = new ArrayList<>();
        String[] s = itemOffset.getValue().toString().split(";");
        for(int i = 0; i < s.length/2; i++) vecs.add(new Vector2((int) Float.parseFloat(s[i*2]), (int) Float.parseFloat(s[i*2+1])));
        writer.writeInt(vecs.size());
        for(Vector2 vec : vecs) writer.writeVector2(vec);

    }

    @Override
    public void load(FileReader reader, int id) {

        itemTexture.setValue(reader.getNextString());
        itemHitboxTexture.setValue(reader.getNextString());
        rightArmTexture.setValue(reader.getNextString());

        StringBuilder s = new StringBuilder();
        int size = reader.getNextInt();
        for(int i = 0; i < size; i++) s.append(reader.getNextFloat()).append(";").append(reader.getNextFloat()).append(";");
        this.itemOffset.setValue(s.toString());

    }

}
