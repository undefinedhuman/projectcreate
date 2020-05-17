package editorUI;

import de.undefinedhuman.sandboxgame.editor.editor.EditorType;
import de.undefinedhuman.sandboxgame.editor.editor.item.ItemEditor;
import de.undefinedhuman.sandboxgame.editor.engine.window.Window;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ItemEditorLoadEditTest {

    private static String[] resetSettings = {
            "SVRFTQ==",
            "MTE=",
            "SUQ=;MzI3Njg=",
            "TmFtZQ==;VGVzdCBJdGVt",
            "RGVzY3JpcHRpb24=;VGVzdCBEZXNj",
            "VGV4dHVyZQ==;VW5rbm93bi5wbmc=",
            "SWNvbg==;VW5rbm93bi5wbmc=",
            "UHJldmlldw==;VW5rbm93bi5wbmc=",
            "VXNlSWNvbkluSGFuZA==;MA==",
            "TWF4QW1vdW50;OTk5",
            "U3RhY2thYmxl;MA==",
            "U2hha2U=;MQ==",
            "UmFyaXR5;TEVHRU5EQVJZ"
    };

    @BeforeAll
    static void initAll() {
        Window.instance = new Window();
    }

    @Test
    public void LoadAndEditItem() {

        Window.instance.setEditor(EditorType.ITEM);
        assertTrue(Window.instance.editor instanceof ItemEditor);
        ItemEditor itemEditor = (ItemEditor) Window.instance.editor;
        TestUtils.sleep(1000);

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Start"));
        TestUtils.sleep();

        assertFalse(Window.instance.fileMenu.isPopupMenuVisible());
        Window.instance.fileMenu.doClick();
        assertTrue(Window.instance.fileMenu.isPopupMenuVisible());
        TestUtils.sleep();

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Press_File_Menu"));
        TestUtils.sleep();

        Component menuComponent = Window.instance.fileMenu.getMenuComponent(0);
        assertNotNull(menuComponent);
        assertTrue(menuComponent instanceof JMenuItem);
        JMenuItem loadItem = (JMenuItem) menuComponent;
        assertEquals("Load", loadItem.getText());
        loadItem.doClick();
        TestUtils.sleep();

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Press_Load"));
        assertNotNull(itemEditor.loadWindow);
        assertTrue(itemEditor.loadWindow.isVisible());
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(itemEditor.loadWindow), "Item_Editor_Load_Window"));
        TestUtils.sleep();

        itemEditor.itemSelection.setSelectedIndex(itemEditor.itemSelection.getItemCount()-1);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(itemEditor.loadWindow), "Item_Editor_Load_Window_Select_Test_Item"));
        TestUtils.sleep();

        itemEditor.loadButton.doClick();
        assertFalse(itemEditor.loadWindow.isVisible());
        TestUtils.sleep();

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_After_Test_Item_Load"));
        TestUtils.sleep();

        assertNotNull(itemEditor.currentItem);
        assertTrue(itemEditor.currentItem instanceof Item);
        assertEquals(11, itemEditor.currentItem.getSettings().size());

        itemEditor.currentItem.name.valueField.setText("Test Item 2");
        itemEditor.currentItem.name.setValue("Test Item 2");
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Change_Name"));
        TestUtils.sleep();

        BooleanSetting isStackable = (BooleanSetting) itemEditor.currentItem.isStackable;
        isStackable.checkBox.setSelected(true);
        isStackable.setValue(true);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Change_IsStackable_True"));
        TestUtils.sleep();

        BooleanSetting canShake = (BooleanSetting) itemEditor.currentItem.canShake;
        canShake.checkBox.setSelected(false);
        canShake.setValue(false);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Change_CanShake_False"));
        TestUtils.sleep();

        SelectionSetting rarity = (SelectionSetting) itemEditor.currentItem.rarity;
        rarity.selection.setSelectedIndex(rarity.selection.getItemCount()-2);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Change_Rarity"));
        TestUtils.sleep();

        menuComponent = Window.instance.fileMenu.getMenuComponent(1);
        assertNotNull(menuComponent);
        assertTrue(menuComponent instanceof JMenuItem);
        JMenuItem saveItem = (JMenuItem) menuComponent;
        assertEquals("Save", saveItem.getText());
        saveItem.doClick();
        TestUtils.sleep();

        itemEditor.reset();
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_After_Save_And_Reset"));
        TestUtils.sleep();

        assertFalse(Window.instance.fileMenu.isPopupMenuVisible());
        Window.instance.fileMenu.doClick();
        assertTrue(Window.instance.fileMenu.isPopupMenuVisible());
        TestUtils.sleep();

        menuComponent = Window.instance.fileMenu.getMenuComponent(0);
        loadItem = (JMenuItem) menuComponent;
        loadItem.doClick();
        TestUtils.sleep();
        itemEditor.itemSelection.setSelectedIndex(itemEditor.itemSelection.getItemCount()-1);
        TestUtils.sleep();
        itemEditor.loadButton.doClick();
        TestUtils.sleep();

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_After_Loading_Saved_Item"));
        TestUtils.sleep();

        Log.test("Item Editor - Load and edit Item - E2E - PASSED");
    }

    @AfterAll
    static void afterAll() {
        FsFile itemFile = new FsFile("items/32768/settings.item", false);
        FileWriter writer = itemFile.getFileWriter(false);
        for(String s : resetSettings) {
            writer.writeString(s);
            writer.nextLine();
        }
        writer.close();
        Log.instance.save();
    }

}
