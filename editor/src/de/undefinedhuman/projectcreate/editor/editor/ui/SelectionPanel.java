package de.undefinedhuman.projectcreate.editor.editor.ui;

import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.ResizeListener;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.ds.Key;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SelectionPanel<T> extends JPanel {

    private T[] currentData;
    private DefaultListModel<T> listModel;
    private JList<T> itemList;
    private JTextField filter;
    private Function<Key<String, T[]>, T[]>[] filters;

    public SelectionPanel(String title, Function<Key<String, T[]>, T[]>... filters) {
        this(title, 0.5f, filters);
    }

    public SelectionPanel(String title, float creationPanelHeight, Function<Key<String, T[]>, T[]>... filters) {
        super(null);
        this.filters = filters;
        setLayout(new RelativeLayout(RelativeLayout.Y_AXIS, 0).setFill(true));
        add(createTitleLabel(title), 0.5f);
        add(filter = createFilter(), 0.35f);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(itemList = createList(currentData = getListData()));
        add(scrollPane, 9.15f - creationPanelHeight);
        add(createCreationPanel(), creationPanelHeight);
    }

    public void init() {
        itemList.setSelectedIndex(0);
    }

    private JLabel createTitleLabel(String title) {
        JLabel label = new JLabel(title);
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

    private JList<T> createList(T[] data) {
        listModel = new DefaultListModel<>();
        for(T date : data)
            listModel.addElement(date);
        JList<T> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addComponentListener(new ResizeListener(0, 5, () -> {
            if(list.getComponentCount() <= 0)
                return "";
            return list.getComponent(0).toString();
        }));
        list.addListSelectionListener(e -> {
            T selectedID = list.getSelectedValue();
            if(selectedID == null)
                return;
            select(selectedID);
        });
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                T id = (T) value;
                if(c instanceof JLabel)
                    ((JLabel) c).setText(getTitle(id));
                c.setBackground(index % 2 == 0 ? c.getBackground() : c.getBackground().darker());
                return c;
            }
        });
        return list;
    }

    public void updateList(String filter, T[] data) {
        listModel.removeAllElements();
        if(filter.equalsIgnoreCase("")) {
            for(T t : data)
                if(!listModel.contains(t)) listModel.addElement(t);
            return;
        }
        for(Function<Key<String, T[]>, T[]> filterFunction : filters) {
            T[] passedElements = filterFunction.apply(new Key<>(filter, data));
            for(T t : passedElements)
                if(!listModel.contains(t)) listModel.addElement(t);
        }
        for(T t : Arrays.stream(data).filter(t -> getTitle(t).toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList()))
            if(!listModel.contains(t)) listModel.addElement(t);
    }

    public void updateData() {
        this.currentData = getListData();
        updateList(filter != null ? filter.getText() : "", currentData);
    }

    public int getSelectedIndex() {
        return itemList.getSelectedIndex();
    }

    public List<T> getSelectedItems() {
        return Arrays.stream(itemList.getSelectedIndices()).mapToObj(value -> listModel.getElementAt(value)).collect(Collectors.toList());
    }

    public List<T> removeSelected() {
        int[] selectedIndices = itemList.getSelectedIndices();
        List<T> removedElements = new ArrayList<>();
        for(int i = selectedIndices.length-1; i >=0; i--)
            removedElements.add(listModel.remove(selectedIndices[i]));
        if(selectedIndices.length != 0) {
            itemList.setSelectedIndex(selectedIndices[selectedIndices.length-1] >= listModel.size() ? 0 : selectedIndices[selectedIndices.length-1]);
            select(itemList.getSelectedValue());
        }
        return removedElements;
    }

    public abstract JPanel createCreationPanel();

    public abstract void add();

    public abstract void select(T t);

    public abstract T[] getListData();

    public abstract String getTitle(T t);

}