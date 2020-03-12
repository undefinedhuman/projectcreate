package me.gentlexd.sandboxeditor.items.type;

import com.badlogic.gdx.math.Vector3;
import me.gentlexd.sandboxeditor.editor.settings.*;
import me.gentlexd.sandboxeditor.items.Item;
import me.gentlexd.sandboxeditor.items.ItemType;

import javax.swing.*;
import java.util.ArrayList;

public class Block extends Item {

    public Block() {}

    public void getSettings(ItemType type, ArrayList<Setting> settings, JPanel mainPanel, JPanel settingsPanel) {

        super.getSettings(type, settings, mainPanel, settingsPanel);
        settings.add(new BlockTextureSetting(mainPanel, "Atlas-Name", "Dirt"));
        settings.add(new SoundSetting(mainPanel, "Sound", "dirtSound.wav"));
        settings.add(new IntSetting(settingsPanel, "Durability", 0));
        settings.add(new IntSetting(settingsPanel, "Drop-ID", 0));
        settings.add(new BooleanSetting(settingsPanel, "Unbreakable", false));
        settings.add(new BooleanSetting(settingsPanel, "Collide", false));
        settings.add(new BooleanSetting(settingsPanel, "Fluid", false));
        settings.add(new BooleanSetting(settingsPanel, "Animated", false));
        settings.add(new BooleanSetting(settingsPanel, "Has Light", false));
        settings.add(new BooleanSetting(settingsPanel, "Has States", false));
        settings.add(new BooleanSetting(settingsPanel, "IsFull", false));
        settings.add(new BooleanSetting(settingsPanel, "PlacedInBackL", true));
        settings.add(new LightSetting(settingsPanel, "Light Color", new Vector3(0,0,0)));

    }

}
