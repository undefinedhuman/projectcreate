package item;

import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.Rarity;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemLoadingTest {

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();
    }

    @Test
    public void BlueprintLoading() {

        FsFile itemFile = new FsFile("test/32768/settings.item", false);
        assertNotNull(itemFile.getFile());

        Item item = ItemManager.loadItem(itemFile);

        assertEquals(32768, item.id.getInt());
        assertEquals("Test Item", item.name.getString());
        assertEquals("Test Desc", item.desc.getString());
        assertEquals("Unknown.png", item.iconTexture.getString());
        assertEquals("Unknown.png", item.itemTexture.getString());
        assertEquals("Unknown.png", item.previewTexture.getString());
        assertFalse(item.useIconAsHandTexture.getBoolean());
        assertEquals(999, item.maxAmount.getInt());
        assertFalse(item.isStackable.getBoolean());
        assertTrue(item.canShake.getBoolean());
        assertEquals(Rarity.LEGENDARY, item.rarity.getRarity());

        Log.test("Item loading - PASSED");
    }

    @AfterAll
    static void afterAll() {
        Log.instance.save();
    }

}
