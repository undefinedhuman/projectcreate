package de.undefinedhuman.projectcreate.engine.settings.panels;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.Arrays;

public abstract class SelectionPanel<K extends Comparable<K>, T extends PanelObject<K>> extends Panel<K, T> {

    private DefaultComboBoxModel<K> comboBoxModel = new DefaultComboBoxModel<>();
    private JComboBox<K> selection;

    public SelectionPanel(String name, Class<T> panelObjectClass) {
        super(name, panelObjectClass);
    }

    @Override
    protected void createPanelObjectNameComponent(JPanel panel) {
        setSelectionData();
        selection = new JComboBox<>(comboBoxModel);
        selection.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(c instanceof JLabel && value != null) {
                    renderJLabel((JLabel) c, (K) value);
                }
                return c;
            }
        });
        selection.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                K selectedItem = (K) selection.getSelectedItem();
                K[] values = setSelectionData();
                selection.setSelectedItem(Utils.hasValue(values, selectedItem) ? selectedItem : values[0]);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        selection.setFont(selection.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        selection.setPreferredSize(new Dimension(0, Panel.INPUT_HEIGHT));
        panel.add(selection);
    }

    @Override
    protected K getSelectedKey() {
        if(selection.getSelectedItem() == null) {
            Log.error("Please select a object from the combo box!");
            return null;
        }
        return (K) selection.getSelectedItem();
    }

    @Override
    public void selectObject(T object, Accordion panel) {
        selection.setSelectedItem(object.getKey());
        for(Setting<?> setting : object.getSettings())
            setting.createSettingUI(panel);
    }

    public abstract void renderJLabel(JLabel label, K k);

    public abstract K[] getSelectionData();

    private K[] setSelectionData() {
        comboBoxModel.removeAllElements();
        K[] data = getSelectionData();
        Arrays.stream(data).forEach(key -> comboBoxModel.addElement(key));
        return data;
    }

}
