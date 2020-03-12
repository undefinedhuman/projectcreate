package me.gentlexd.sandboxeditor.items.type;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.items.ItemType;

import javax.swing.*;
import java.util.ArrayList;

public class Sword extends Weapon {

    public Sword() {}

    public void getSettings(ItemType type, ArrayList<Setting> settings, JPanel mainPanel, JPanel settingsPanel) {

        super.getSettings(type, settings, mainPanel, settingsPanel);
        settings.add(new FloatSetting(settingsPanel, "Damage", 0));
        settings.add(new FloatSetting(settingsPanel, "Speed", 0));

    }

}
