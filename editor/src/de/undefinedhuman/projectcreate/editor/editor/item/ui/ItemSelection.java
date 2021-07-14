package de.undefinedhuman.projectcreate.editor.editor.item.ui;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class ItemSelection<T extends Enum<T>> extends JPanel {

    private Integer[] currentData;
    private DefaultListModel<Integer> listModel;
    private JList<Integer> itemList;
    private JComboBox<T> itemSelection;
    private JTextField filter;

    public ItemSelection(T[] types) {
        super(null);
        setLayout(new RelativeLayout(RelativeLayout.Y_AXIS, 0).setFill(true));
        add(createTitleLabel(), 0.5f);
        add(filter = createFilter(), 0.35f);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(itemList = createItemList(currentData = getListData()));
        add(scrollPane, 8.65f);
        add(createItemCreationPanel(types), 0.5f);
    }

    public void init() {
        itemList.setSelectedIndex(0);
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel("Items");
        label.addComponentListener(new ResizeListener(10, 0, label::getText));
        label.setOpaque(true);
        label.setBackground(Variables.BACKGROUND_COLOR.darker());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        return label;
    }

    private JTextField createFilter() {
        JTextField filter = new JTextField();
        filter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                updateList(filter.getText(), currentData);
            }
        });
        filter.addComponentListener(new ResizeListener(15, 0, () -> "PLACEHOLDER"));
        return filter;
    }

    private JList<Integer> createItemList(Integer[] data) {
        listModel = new DefaultListModel<>();
        for(Integer id : data)
            listModel.addElement(id);
        JList<Integer> itemList = new JList<>(listModel);
        itemList.addComponentListener(new ResizeListener(0, 5, () -> {
            if(itemList.getComponentCount() <= 0)
                return "";
            return itemList.getComponent(0).toString();
        }));
        itemList.addListSelectionListener(e -> {
            Integer selectedID = itemList.getSelectedValue();
            if(selectedID == null)
                return;
            selectItem(selectedID);
        });
        itemList.setCellRenderer(new ListRenderer());
        return itemList;
    }

    private JPanel createItemCreationPanel(T... types) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true).setFillGap(10));
        panel.add(itemSelection = createItemTypeSelection(types), 0.625f);
        panel.add(createNewButton(), 0.375f);
        return panel;
    }

    private JComboBox<T> createItemTypeSelection(T... types) {
        JComboBox<T> itemTypeSelection = new JComboBox<>(Arrays.stream(types).sorted(Comparator.comparing(Enum::name)).collect(Collectors.toList()).toArray((T[]) Array.newInstance(types.getClass().getComponentType(), types.length)));
        itemTypeSelection.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                T type = (T) value;
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
            return Arrays.stream(types).map(Enum::name).sorted(Comparator.comparingInt(String::length)).reduce((a, b) -> b).orElse("");
        }));
        return itemTypeSelection;
    }

    private JButton createNewButton() {
        JButton button = new JButton("Create");
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.addComponentListener(new ResizeListener(15, 0, button::getText));
        button.setFont(button.getFont().deriveFont(25f).deriveFont(Font.BOLD));
        button.addActionListener(e -> {
            addItem((T) itemSelection.getSelectedItem());
            updateData();
        });
        return button;
    }

    public void updateList(String filter, Integer[] data) {
        listModel.removeAllElements();
        if(filter.equalsIgnoreCase("")) {
            for(Integer id : data)
                listModel.addElement(id);
            return;
        }
        if(filter.matches("^[0-9]+-[0-9]+$")) {
            String[] ids = filter.split("-");
            Integer lower = Tools.isInteger(ids[0]), upper = Tools.isInteger(ids[1]);
            if(lower != null && upper != null && lower <= upper) {
                for(Integer id : Arrays.stream(data).filter(itemID -> Tools.isInRange(itemID, lower, upper)).toArray(Integer[]::new))
                    listModel.addElement(id);
                return;
            }
        }
        for(Integer id : Arrays.stream(data).filter(itemID -> {
            if(!ItemManager.getInstance().hasItem(itemID))
                return false;
            Item item = ItemManager.getInstance().getItem(itemID);
            return (itemID + " " + item.name.getValue()).toLowerCase().contains(filter.toLowerCase());
        }).toArray(Integer[]::new))
            listModel.addElement(id);
    }

    public void updateData() {
        this.currentData = getListData();
        updateList(filter != null ? filter.getText() : "", currentData);
    }

    public int getSelectedItemID() {
        if(itemList.getSelectedValue() == null)
            return -1;
        return itemList.getSelectedValue();
    }

    public void removeSelectedItem() {
        int selectedIndex = itemList.getSelectedIndex();
        if(selectedIndex == -1)
            return;
        listModel.remove(selectedIndex);
        itemList.setSelectedIndex(selectedIndex >= listModel.size() ? 0 : selectedIndex);
        selectItem(itemList.getSelectedValue());
    }

    public abstract void addItem(T type);

    public abstract void selectItem(int id);

    public abstract Integer[] getListData();

    private static class ListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Integer id = (Integer) value;
            if(c instanceof JLabel) {
                JLabel label = (JLabel) c;
                Item item = ItemManager.getInstance().getItem(id);
                if(item != null)
                    label.setText(id + " " + item.name.getValue());
            }
            c.setBackground(index % 2 == 0 ? c.getBackground() : c.getBackground().darker());
            return c;
        }
    }

}