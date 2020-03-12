package me.gentlexd.sandboxeditor.items;
import me.gentlexd.sandboxeditor.editor.settings.*;

import javax.swing.*;
import java.util.ArrayList;

public class Item {

    public Item() {}

    public void getSettings(ItemType type, ArrayList<Setting> settings, JPanel mainPanel, JPanel settingsPanel) {

        settings.add(new IntSetting(mainPanel, "ID", 0));
        if(type != ItemType.BLOCK) {
            settings.add(new TextureSetting(mainPanel, "Texture", "Unknown.png"));
            settings.add(new TextureSetting(mainPanel, "Icon", "Unknown.png"));
        }
        settings.add(new StringSetting(settingsPanel, "Name", "Default"));
        settings.add(new StringSetting(settingsPanel, "Description", "It's a default Item!"));
        settings.add(new BooleanSetting(settingsPanel, "Stackable?", false));
        settings.add(new IntSetting(settingsPanel, "MaxAmount", 0));
        settings.add(new BooleanSetting(settingsPanel, "Shake?", false));

    }

}
