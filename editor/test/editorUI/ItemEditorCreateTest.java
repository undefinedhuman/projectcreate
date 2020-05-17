package editorUI;

import de.undefinedhuman.sandboxgame.editor.editor.EditorType;
import de.undefinedhuman.sandboxgame.editor.editor.item.ItemEditor;
import de.undefinedhuman.sandboxgame.editor.engine.window.Window;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ItemEditorCreateTest {

    @BeforeAll
    static void initAll() {
        Window.instance = new Window();
    }

    @Test
    public void CreateItem() {

        Window.instance.setEditor(EditorType.ITEM);
        assertTrue(Window.instance.editor instanceof ItemEditor);
        ItemEditor itemEditor = (ItemEditor) Window.instance.editor;
        TestUtils.sleep(1000);

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Start"));
        TestUtils.sleep();

        itemEditor.itemComboBox.setSelectedIndex(0);
        assertNotNull(itemEditor.currentItem);
        assertTrue(itemEditor.currentItem instanceof Item);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_New_Item"));
        TestUtils.sleep();

        itemEditor.currentItem.id.valueField.setText("32767");
        itemEditor.currentItem.id.setValue("32767");
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_Edit_ID"));
        TestUtils.sleep();

        itemEditor.currentItem.name.valueField.setText("Test Item New");
        itemEditor.currentItem.name.setValue("Test Item New");
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_Edit_Name"));
        TestUtils.sleep();

        BooleanSetting useIconAsHandTexture = (BooleanSetting) itemEditor.currentItem.useIconAsHandTexture;
        useIconAsHandTexture.checkBox.setSelected(true);
        useIconAsHandTexture.setValue(true);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_Edit_useIconAsHandTexture"));
        TestUtils.sleep();

        SelectionSetting rarity = (SelectionSetting) itemEditor.currentItem.rarity;
        rarity.selection.setSelectedIndex(rarity.selection.getItemCount()-1);
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_Edit_Rarity"));
        TestUtils.sleep();

        Component menuComponent = Window.instance.fileMenu.getMenuComponent(1);
        assertNotNull(menuComponent);
        assertTrue(menuComponent instanceof JMenuItem);
        JMenuItem saveItem = (JMenuItem) menuComponent;
        assertEquals("Save", saveItem.getText());
        saveItem.doClick();
        TestUtils.sleep();

        itemEditor.reset();
        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_Save_And_Reset"));
        TestUtils.sleep();

        assertFalse(Window.instance.fileMenu.isPopupMenuVisible());
        Window.instance.fileMenu.doClick();
        assertTrue(Window.instance.fileMenu.isPopupMenuVisible());
        TestUtils.sleep();

        menuComponent = Window.instance.fileMenu.getMenuComponent(0);
        JMenuItem loadItem = (JMenuItem) menuComponent;
        loadItem.doClick();
        TestUtils.sleep();
        itemEditor.itemSelection.setSelectedIndex(itemEditor.itemSelection.getItemCount()-2);
        TestUtils.sleep();
        itemEditor.loadButton.doClick();
        TestUtils.sleep();

        Log.info(TestUtils.compareScreenshots(TestUtils.screenshot(Window.instance), "Item_Editor_Create_After_Loading_Saved_Item"));
        TestUtils.sleep();

        Log.test("Item Editor - Create Item - E2E - PASSED");
    }

    @AfterAll
    static void afterAll() {
        FsFile file = new FsFile("items/32767/", true);
        if(file.exists()) FileUtils.deleteFile(file);
        Log.instance.save();
    }

}
