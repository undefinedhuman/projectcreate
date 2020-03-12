package me.gentlexd.sandboxeditor.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.editor.settings.TextureSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class RightArmComponent extends Component {

    private JPanel panel;

    private StringSetting textureName;
    private TextureSetting selectedTexture;
    private Vector2Setting turnedOffset, origin, shoulderPosOffset;

    public RightArmComponent(JPanel panel, String name) {

        super(panel, name);
        this.panel = panel;

        textureName = new StringSetting(panel,"Texture Data","Temp", false);
        turnedOffset = new Vector2Setting(panel,"Turned Offset", new Vector2(0,0), false);
        origin = new Vector2Setting(panel,"Origin", new Vector2(0,0), false);
        shoulderPosOffset = new Vector2Setting(panel,"Shoulder Pos", new Vector2(0,0), false);
        selectedTexture = new TextureSetting(panel,"Selected Texture","",false);

    }

    @Override
    public void addMenuComponent() {

        panel.revalidate();
        panel.repaint();

        textureName.addToMenu(new Vector2(0,0));
        turnedOffset.addToMenu(new Vector2(0,30));
        origin.addToMenu(new Vector2(0,60));
        shoulderPosOffset.addToMenu(new Vector2(0,90));
        selectedTexture.addToMenu(new Vector2(0,120));

    }

    @Override
    public void removeMenuComponent() {

        panel.removeAll();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(textureName.getValue().toString());
        writer.writeVector2(turnedOffset.getVector());
        writer.writeVector2(origin.getVector());
        writer.writeVector2(shoulderPosOffset.getVector());
        selectedTexture.save(writer);

    }

    @Override
    public void load(FileReader reader, int id) {

        textureName.setValue(reader.getNextString());
        turnedOffset.setValue(reader.getNextVector2());
        origin.setValue(reader.getNextVector2());
        shoulderPosOffset.setValue(reader.getNextVector2());
        selectedTexture.load(reader, id);

    }

}
