package de.undefinedhuman.projectcreate.engine.settings.types.selection;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;
import de.undefinedhuman.projectcreate.engine.settings.types.BaseSetting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Supplier;

public class DynamicSelectionSetting<T> extends BaseSetting<T> {

    private DefaultComboBoxModel<T> comboBoxModel;
    private JComboBox<T> selection;
    private Supplier<T[]> getValues;

    public DynamicSelectionSetting(String key, T defaultValue, Supplier<T[]> getValues, Parser<T> parser, Serializer<T> serializer) {
        super(key, defaultValue, parser, serializer);
        this.getValues = getValues;
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        T[] values = getValues.get();
        comboBoxModel = new DefaultComboBoxModel<>();
        Arrays.stream(values).forEach(comboBoxModel::addElement);
        selection = new JComboBox<>(comboBoxModel);
        selection.setFont(selection.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        if(Utils.hasValue(values, getValue()))
            selection.setSelectedItem(getValue());
        selection.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                T selectedItem = getValue();
                comboBoxModel.removeAllElements();
                T[] values = getValues.get();
                if(values.length == 0)
                    return;
                Arrays.stream(values).forEach(comboBoxModel::addElement);
                selection.setSelectedItem(Utils.hasValue(values, selectedItem) ? selectedItem : values[0]);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        selection.addActionListener(e -> {
            if(selection.getSelectedItem() == null)
                return;
            setValue((T) selection.getSelectedItem());
        });
        accordion.addInlinePanel(key, selection);
    }

    @Override
    protected void loadValue(FileHandle parentDir, Object value) {
        super.loadValue(parentDir, value);
        T[] values = getValues.get();
        if(Utils.hasValue(values, getValue()) || values.length == 0)
            return;
        setValue(values[0]);
    }

    @Override
    protected void updateMenu(T value) {
        if(selection != null)
            selection.setSelectedItem(serializer.serialize(value));
    }

}
