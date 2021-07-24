package de.undefinedhuman.projectcreate.editor.editor.item.ui;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.editor.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class ItemSettingsPanel extends JPanel {

    private Accordion generalSettings, textureSettings, typeSettings;

    public ItemSettingsPanel() {
        super(new RelativeLayout(RelativeLayout.X_AXIS, 40).setBorderGap(40).setFill(true));
        add(generalSettings = new Accordion(Variables.BACKGROUND_COLOR), 0.25f);
        add(textureSettings = new Accordion(Variables.BACKGROUND_COLOR), 0.25f);
        add(typeSettings = new Accordion(Variables.BACKGROUND_COLOR), 0.25f);
        add(Box.createGlue(), 0.25f);
    }

    public void updateItem(int id) {
        if(!ItemManager.getInstance().hasItem(id))
            return;
        Item item = ItemManager.getInstance().getItem(id);
        clear();
        Utils.addSettingsGroupToAccordion(item.getGeneralSettings(), generalSettings);
        Utils.addSettingsGroupToAccordion(item.getTextureSettings(), textureSettings);
        Utils.addSettingsGroupToAccordion(item.getRecipeSettings(), generalSettings);
        for(SettingsGroup settingsGroup : item.getSettingsGroups())
            Utils.addSettingsGroupToAccordion(settingsGroup, typeSettings);
    }

    public void clear() {
        generalSettings.removeAll();
        textureSettings.removeAll();
        typeSettings.removeAll();
    }

}
