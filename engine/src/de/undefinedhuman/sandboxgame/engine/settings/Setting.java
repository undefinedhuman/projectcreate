package de.undefinedhuman.sandboxgame.engine.settings;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.EntityType;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.Rarity;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.BlockType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Setting {

    public float offset = 25;

    protected String key;
    protected Object value;

    private JTextField valueField;
    private SettingType type;

    public Setting(SettingType type, String key, Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    public String getString() { return String.valueOf(value); }
    public float getFloat() { return Float.parseFloat(getString()); }
    public boolean getBoolean() { return FileUtils.readBoolean(getString()); }
    public int getInt() { return Integer.parseInt(getString()); }

    public Vector2 getVector2() { return (Vector2) value; }

    public int getInputKey() { return Input.Keys.valueOf(getString()); }

    public String[] getStringArray() { return (String[]) value; }
    public Vector2[] getVector2Array() { return (Vector2[]) value; }

    public Rarity getRarity() { return (Rarity) Rarity.valueOf(getString()); }
    public Animation.PlayMode getPlayMode() {
        return Animation.PlayMode.valueOf(getString());
    }
    public EntityType getEntityType() { return EntityType.valueOf(getString()); }
    public BlockType getBlockType() { return BlockType.valueOf(getString()); }

    public SettingType getType() { return type; }

    public void loadSetting(FsFile parentDir, HashMap<String, LineSplitter> settings) {
        if(!settings.containsKey(key)) return;
        load(parentDir, settings.get(key));
        setValueInMenu(value);
    }
    public void saveSetting(FsFile parentDir, FileWriter writer) {
        writer.writeString(key);
        save(parentDir, writer);
    }

    public void addMenuComponents(JPanel panel, Vector2 position) {
        JLabel keyLabel = new JLabel(key + ": (" + type.name() + ")", SwingConstants.CENTER);
        keyLabel.setBounds((int) position.x, (int) position.y, 170, 25);
        keyLabel.setBackground(Color.LIGHT_GRAY);
        keyLabel.setOpaque(true);
        panel.add(keyLabel);
        addValueMenuComponents(panel, new Vector2(position).add(180, 0));
    }

    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        valueField = createTextField(value, position, new Vector2(200, 25), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(valueField.getText() == null || valueField.getText().equalsIgnoreCase("")) return;
                setValue(valueField.getText());
            }
        });
        panel.add(valueField);
    }

    protected JTextField createTextField(Object value, Vector2 position, Vector2 size, KeyAdapter adapter) {
        JTextField textField = new JTextField(String.valueOf(value));
        textField.setBounds((int) position.x, (int) position.y, (int) size.x, (int) size.y);
        textField.addKeyListener(adapter);
        return textField;
    }

    protected void setValueInMenu(Object value) {
        if(valueField != null) valueField.setText(value.toString());
    }

    protected void load(FsFile parentDir, LineSplitter splitter) {
        value = splitter.getNextString();
    }
    protected void save(FsFile parentDir, FileWriter writer) {
        writer.writeString(value.toString());
    }

    protected void delete() { value = null; }

}
