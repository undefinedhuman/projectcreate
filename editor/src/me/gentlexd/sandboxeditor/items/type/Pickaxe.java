package me.gentlexd.sandboxeditor.items.type;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.IntSetting;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.items.ItemType;

import javax.swing.*;
import java.util.ArrayList;

public class Pickaxe extends Tool {

    public Pickaxe() {}

    public void getSettings(ItemType type, ArrayList<Setting> settings, JPanel mainPanel, JPanel settingsPanel) {

        super.getSettings(type, settings, mainPanel, settingsPanel);
        settings.add(new FloatSetting(settingsPanel, "Speed",0));
        settings.add(new FloatSetting(settingsPanel, "Strength",0));
        settings.add(new IntSetting(settingsPanel, "BlockRange",6));

    }

}
