package de.undefinedhuman.projectcreate.editor.editor.item.ui;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.editor.editor.ui.SelectionPanel;
import de.undefinedhuman.projectcreate.editor.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public abstract class ItemSelectionPanel extends SelectionPanel<Integer> {

    private JComboBox<ItemType> itemSelection;

    public ItemSelectionPanel() {
        super("Items", key -> {
            if(key.getKey1().matches("^[0-9]+-[0-9]+$")) {
                String[] ids = key.getKey1().split("-");
                Integer lower = Tools.isInteger(ids[0]), upper = Tools.isInteger(ids[1]);
                if(lower != null && upper != null && lower <= upper)
                    return Arrays.stream(key.getKey2()).filter(itemID -> Tools.isInRange(itemID, lower, upper)).toArray(Integer[]::new);
            }
            return new Integer[0];
        });
    }

    @Override
    public void add() {
        if(itemSelection.getSelectedItem() == null)
            return;
        Integer[] ids = ItemManager.getInstance().getItems().keySet().toArray(new Integer[0]);
        int newID = Utils.findSmallestMissing(ids, 0, ids.length-1);
        Item item = ((ItemType) itemSelection.getSelectedItem()).createInstance();
        item.id.setValue(newID);
        ItemManager.getInstance().addItem(newID, item);
        Utils.saveItem(newID);
    }

    @Override
    public void createMenuPanels(JPanel parentPanel) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true).setFillGap(10));
        panel.add(itemSelection = createItemTypeSelection(), 0.65f);
        panel.add(createNewButton(), 0.35f);
        parentPanel.add(panel, 0.5f);
    }

    private JComboBox<ItemType> createItemTypeSelection() {
        JComboBox<ItemType> itemTypeSelection = new JComboBox<>(Arrays.stream(ItemType.values()).sorted(Comparator.comparing(Enum::name)).toArray(ItemType[]::new));
        itemTypeSelection.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                ItemType type = (ItemType) value;
                if(c instanceof JLabel && type != null) {
                    JLabel label = (JLabel) c;
                    String name = type.name();
                    label.setText(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                }
                return c;
            }
        });
        itemTypeSelection.addComponentListener(new ResizeListener(10, 0, () -> {
            if(itemTypeSelection.getItemCount() <= 0)
                return "";
            return Arrays.stream(ItemType.values()).map(Enum::name).sorted(Comparator.comparingInt(String::length)).reduce((a, b) -> b).orElse("");
        }));
        return itemTypeSelection;
    }

    private JButton createNewButton() {
        JButton button = new JButton("ADD");
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.addComponentListener(new ResizeListener(15, 0, button::getText));
        button.setFont(button.getFont().deriveFont(25f).deriveFont(Font.BOLD));
        button.addActionListener(e -> {
            add();
            updateData();
        });
        return button;
    }

    @Override
    public Integer[] getListData() {
        return ItemManager
                .getInstance()
                .getItems()
                .keySet()
                .toArray(new Integer[0]);
    }

    @Override
    public String getTitle(Integer id) {
        if(!ItemManager.getInstance().hasItem(id))
            return "ERROR " + " TITLE NOT FOUND, ID: " + id;
        return id + " " + ItemManager.getInstance().getItem(id).name.getValue();
    }

}
