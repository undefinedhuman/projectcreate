package me.gentlexd.sandboxeditor.items.type;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.items.Item;
import me.gentlexd.sandboxeditor.items.ItemType;

import javax.swing.*;
import java.util.ArrayList;

public class Tool extends Item {

    public Tool() {}

    public void getSettings(ItemType type, ArrayList<Setting> settings, JPanel mainPanel, JPanel settingsPanel) {

        super.getSettings(type, settings, mainPanel, settingsPanel);
        settings.add(new FloatSetting(settingsPanel, "Arm Difference", 0));
        settings.add(new FloatSetting(settingsPanel, "Downtime", 0));
        settings.add(new FloatSetting(settingsPanel, "Uptime", 0));

    }

}
