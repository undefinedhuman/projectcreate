package de.undefinedhuman.projectcreate.engine.settings;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.entity.EntityType;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.items.Rarity;
import de.undefinedhuman.projectcreate.engine.items.type.blocks.BlockType;
import de.undefinedhuman.projectcreate.engine.items.type.blocks.PlacementLayer;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.Version;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Setting {

    protected String key;
    protected Object value;
    private int contentHeight = Variables.DEFAULT_CONTENT_HEIGHT;
    protected boolean includeType = true;

    public JTextField valueField;
    private SettingType type;

    public Setting(SettingType type, String key, Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public Setting setIncludeType(boolean includeType) {
        this.includeType = includeType;
        return this;
    }

    public String getKey() { return key; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    public String getString() { return String.valueOf(value); }
    public float getFloat() { return Float.parseFloat(getString()); }
    public boolean getBoolean() { return FileUtils.readBoolean(getString()); }
    public int getInt() { return Integer.parseInt(getString()); }
    public byte getByte() { return Byte.parseByte(getString()); }

    public Vector2 getVector2() { return (Vector2) value; }

    public int getInputKey() { return Input.Keys.valueOf(getString()); }

    public String[] getStringArray() { return (String[]) value; }
    public Vector2[] getVector2Array() { return (Vector2[]) value; }

    public PlacementLayer getPlacementLayer() { return PlacementLayer.valueOf(getString()); }
    public Rarity getRarity() { return Rarity.valueOf(getString()); }
    public Animation.PlayMode getPlayMode() {
        return Animation.PlayMode.valueOf(getString());
    }
    public EntityType getEntityType() { return EntityType.valueOf(getString()); }
    public BlockType getBlockType() { return BlockType.valueOf(getString()); }
    public Version getVersion() { return Version.parse(getString()); }
    public FsFile getFile() { return new FsFile(getString(), Files.FileType.Absolute); }

    public int getTotalHeight() {
        return contentHeight + Variables.BORDER_HEIGHT;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    protected void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }

    public SettingType getType() { return type; }

    public void loadSetting(FileHandle parentDir, SettingsObject settingsObject) {
        if(!settingsObject.containsKey(key)) return;
        loadValue(parentDir, settingsObject.get(key));
        setValueInMenu(value);
    }

    public void save(FileWriter writer) {
        writer.writeString(key);
        writer.writeString(value.toString());
    }

    protected void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter)) return;
        setValue(((LineSplitter) value).getNextString());
    }

    public void addMenuComponents(JComponent container, Vector2 position, int containerWidth) {
        int contentWidth = containerWidth - Variables.BORDER_WIDTH;
        JPanel contentPanel = new JPanel(null);
        contentPanel.setSize(contentWidth, contentHeight);

        addValueMenuComponents(contentPanel, contentWidth);

        JScrollPane settingsContainer = new JScrollPane(contentPanel);
        settingsContainer.setBounds((int) position.x, (int) position.y, containerWidth, contentHeight + Variables.BORDER_HEIGHT);
        settingsContainer.setBorder(BorderFactory.createTitledBorder(key + (includeType ? " (" + type.name() + ")" : "")));
        container.add(settingsContainer);
    }

    protected void addValueMenuComponents(JPanel panel, int width) {
        valueField = createTextField(value, new Vector2(0, 0), new Vector2(width, contentHeight), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(valueField.getText() == null || valueField.getText().equalsIgnoreCase(""))
                    return;
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

    protected void delete() { value = null; }

    @Override
    public String toString() {
        return "[" + key + ", " + value + "]";
    }
}
