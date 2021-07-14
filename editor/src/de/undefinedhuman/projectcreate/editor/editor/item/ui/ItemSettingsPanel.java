package de.undefinedhuman.projectcreate.editor.editor.item.ui;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
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

    public void updateItem(Item item) {
        clear();
        addSettingsGroupToAccordion(item.getGeneralSettings(), generalSettings);
        addSettingsGroupToAccordion(item.getTextureSettings(), textureSettings);
        addSettingsGroupToAccordion(item.getRecipeSettings(), generalSettings);
        for(SettingsGroup settingsGroup : item.getSettingsGroups())
            addSettingsGroupToAccordion(settingsGroup, typeSettings);
    }

    private void addSettingsGroupToAccordion(SettingsGroup settingsGroup, Accordion accordion) {
        Accordion settingsAccordion = new Accordion(Variables.BACKGROUND_COLOR.darker()) {
            @Override
            public void update() {
                super.update();
                accordion.update();
            }
        };
        for(Setting<?> setting : settingsGroup.getSettings())
            setting.createSettingUI(settingsAccordion);
        accordion.addCollapsiblePanel(settingsGroup.getTitle(), settingsAccordion);
    }

    public void clear() {
        generalSettings.removeAll();
        textureSettings.removeAll();
        typeSettings.removeAll();
    }

}
