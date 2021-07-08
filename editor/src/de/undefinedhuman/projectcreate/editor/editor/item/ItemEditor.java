package de.undefinedhuman.projectcreate.editor.editor.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.editor.Window;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.editor.editor.item.ui.ItemSelection;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectAdapter;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;
import de.undefinedhuman.projectcreate.engine.settings.ui.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ItemEditor extends Editor {

    private static final int SETTINGS_CONTAINER_WIDTH = 400;

    public JComboBox<ItemType> itemComboBox;
    public Item currentItem;

    public JFrame loadWindow;
    public JComboBox<String> itemSelection;
    public JButton loadButton;

    private Accordion settingsUI;

    public ItemEditor() {
        super();
        //add(SettingsUtils.setLocation(20, 15, settingsUI = new Accordion("Settings", SETTINGS_CONTAINER_WIDTH, SETTINGS_CONTAINER_HEIGHT, Variables.BACKGROUND_COLOR.darker())));

        FsFile itemsDirectory = new FsFile(Paths.ITEM_PATH, Files.FileType.Internal);

        ItemManager.getInstance().loadItems(
                Arrays.stream(itemsDirectory.list(File::isDirectory))
                        .filter(fileHandle -> Tools.isInteger(fileHandle.name()) != null)
                        .mapToInt(fileHandle -> Integer.parseInt(fileHandle.name())).toArray()
        );

        ItemType[] itemTypesSorted = ItemType.values().clone();
        Arrays.sort(itemTypesSorted, (type1, type2) -> type1.name().compareTo(type2.toString()));
        itemComboBox = new JComboBox<>();
        itemComboBox.setBounds(440, 25, 150, 25);
        //itemComboBox.setSelectedIndex(0);
        itemComboBox.addActionListener(e -> {
            if(itemComboBox.getSelectedItem() == null)
                return;
            settingsUI.removeAll();
            ItemType type = ItemType.valueOf(itemComboBox.getSelectedItem().toString());
            currentItem = type.createInstance();
            for(Setting<?> setting : currentItem.getSettings().stream().filter(setting -> !(setting instanceof TextureSetting)).map(setting -> (Setting<?>) setting).collect(Collectors.toList()))
                settingsUI.addPanel(setting.getMenuTitle(), setting.createSettingUI(SETTINGS_CONTAINER_WIDTH - 10));
        });

        //add(itemComboBox);

        JLabel label = new JLabel("Items");
        label.setSize(200, 40);
        label.setOpaque(true);
        label.setBackground(Variables.BACKGROUND_COLOR.darker());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(25f).deriveFont(Font.BOLD));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.1f;
        c.weighty = 1f;
        c.fill = GridBagConstraints.BOTH;

        add(new ItemSelection<ItemType>(ItemType.values()) {
            @Override
            public void addItem(ItemType type) {
                int id = ItemManager.getInstance().getItems().keySet().stream().sorted().reduce((a, b) -> b).orElse(0);
                ItemManager.getInstance().addItem(id + 1, type.createInstance());
            }

            @Override
            public void selectItem(int id) {}

            @Override
            public Integer[] getListData() {
                return ItemManager
                        .getInstance()
                        .getItems()
                        .keySet()
                        .toArray(new Integer[0]);
            }
        }, c);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.YELLOW);
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.95f;
        c.weighty = 1f;
        c.fill = GridBagConstraints.BOTH;

        add(panel, c);

        JList<String> itemList = new JList<>();
        itemList.setBounds(0, 75, 200, EDITOR_PANEL_HEIGHT - 75);
        itemList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(index % 2 == 0 ? c.getBackground() : c.getBackground().darker());
                return c;
            }
        });
        //add(itemList);

    }

    @Override
    public void load() {

        FileHandle[] itemDirs = RessourceUtils.loadDir(Paths.ITEM_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle itemDir : itemDirs) {
            if (!itemDir.isDirectory())
                continue;
            FsFile itemFile = new FsFile(Paths.ITEM_PATH, itemDir.name() + "/settings.item", Files.FileType.Internal);
            if(itemFile.length() == 0)
                continue;
            FileReader reader = itemFile.getFileReader(true);
            SettingsObject settings = new SettingsObjectAdapter(reader);
            ids.add(itemDir.name() + "-" + ((LineSplitter) settings.get("Name")).getNextString());
            reader.close();
        }

        loadWindow = new JFrame("Load Item");
        loadWindow.setSize(480, 150);
        loadWindow.setLocationRelativeTo(null);
        loadWindow.setResizable(false);

        JLabel label = new JLabel();
        loadWindow.add(label);

        loadButton = new JButton("Load Item");
        loadButton.setBounds(90, 70, 300, 25);

        String[] idArray = ids.toArray(new String[0]);
        Arrays.sort(idArray, Comparator.comparing(c -> Integer.valueOf(c.split("-")[0])));

        itemSelection = new JComboBox<>(idArray);
        itemSelection.setBounds(90, 35, 300, 25);

        loadButton.addActionListener(a -> {
            if(itemSelection.getSelectedItem() == null) return;
            FileReader reader = new FileReader(new FsFile(Paths.ITEM_PATH, Integer.parseInt(((String) itemSelection.getSelectedItem()).split("-")[0]) + "/settings.item", Files.FileType.Internal), true);
            SettingsObject settingsObject = new SettingsObjectAdapter(reader);
            if(!settingsObject.containsKey("Type")) return;
            ItemType type = ItemType.valueOf(((LineSplitter) settingsObject.get("Type")).getNextString());
            itemComboBox.setSelectedItem(type);

            settingsUI.removeAll();
            currentItem = type.createInstance();
            for(Setting<?> setting : currentItem.getSettingsList().getSettings())
                setting.load(reader.parent(), settingsObject);
            for(Setting<?> setting : currentItem.getSettings())
                settingsUI.addPanel(setting.getMenuTitle(), setting.createSettingUI(SETTINGS_CONTAINER_WIDTH - 10));
            loadWindow.setVisible(false);
            reader.close();
        });

        label.add(itemSelection);
        label.add(loadButton);
        loadWindow.setVisible(true);
    }

    @Override
    public void createMenuButtonsPanel(JPanel menuButtonPanel) {
        menuButtonPanel.setLayout(new GridLayout(1, 3, 5, 0));
        menuButtonPanel.setMinimumSize(new Dimension(100, Window.MENU_HEIGHT));
        menuButtonPanel.add(new JButton("Save"));
        menuButtonPanel.add(new JButton("Reset"));
        menuButtonPanel.add(new JButton("Delete"));
    }

    @Override
    public void save() {
        if(currentItem == null) return;
        FsFile itemDir = new FsFile(Paths.ITEM_PATH, currentItem.getSettingsList().getSettings().get(0).getValue() + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(itemDir.exists())
            FileUtils.deleteFile(itemDir);

        FileWriter writer = new FsFile(itemDir, "settings.item", Files.FileType.Local).getFileWriter(true);
        writer.writeString("Type").writeString(currentItem.type.name());
        writer.nextLine();
        Tools.saveSettings(writer, currentItem.getSettingsList());
        writer.close();
    }

}
