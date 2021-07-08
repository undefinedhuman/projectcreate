package de.undefinedhuman.projectcreate.editor.editor.item.ui;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.editor.editor.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.editor.editor.ui.listener.ResizeListener;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public abstract class ItemSelection<T extends Enum<T>> extends JPanel {

    private Integer[] currentData;
    private JList<Integer> itemList;

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
        JList<Integer> itemList = new JList<>(data);
        itemList.addComponentListener(new ResizeListener(0, 5, () -> {
            if(itemList.getComponentCount() <= 0)
                return "";
            return itemList.getComponent(0).toString();
        }));
        itemList.setCellRenderer(new ListRenderer());
        return itemList;
    }

    private JPanel createItemCreationPanel(T... types) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true).setFillGap(10));
        panel.add(createItemTypeSelection(types), 0.6f);
        panel.add(Box.createGlue(), 0.05f);
        panel.add(createNewButton(), 0.35f);
        return panel;
    }

    private JComboBox<String> createItemTypeSelection(T... types) {
        JComboBox<String> itemTypeSelection = new JComboBox<>(Arrays.stream(types.clone()).map(itemType -> itemType.name().charAt(0) + itemType.name().substring(1).toLowerCase()).sorted().toArray(String[]::new));
        itemTypeSelection.setRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                return null;
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
        JButton button = new JButton("Create Item");
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.addComponentListener(new ResizeListener(15, 0, button::getText));
        button.setFont(button.getFont().deriveFont(25f).deriveFont(Font.BOLD));
        button.addActionListener(e -> {
            addItem();
            updateData();
        });
        return button;
    }

    public void updateList(String filter, Integer[] data) {
        itemList.removeAll();
        if(filter.equalsIgnoreCase("")) {
            itemList.setListData(data);
            return;
        }
        if(filter.matches("^[0-9]+-[0-9]+$")) {
            String[] ids = filter.split("-");
            Integer lower = Tools.isInteger(ids[0]), upper = Tools.isInteger(ids[1]);
            if(lower != null && upper != null && lower <= upper) {
                itemList.setListData(Arrays.stream(data).filter(itemID -> Tools.isInRange(itemID, lower, upper)).toArray(Integer[]::new));
                return;
            }
        }
        itemList.setListData(Arrays.stream(data).filter(itemID -> {
            if(!ItemManager.getInstance().hasItem(itemID))
                return false;
            Item item = ItemManager.getInstance().getItem(itemID);
            return (itemID + " " + item.name.getValue()).toLowerCase().contains(filter.toLowerCase());
        }).toArray(Integer[]::new));
    }

    public void updateData() {
        this.currentData = getListData();
        updateList(filter != null ? filter.getText() : "", currentData);
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
