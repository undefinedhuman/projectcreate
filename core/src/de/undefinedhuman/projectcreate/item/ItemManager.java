package de.undefinedhuman.projectcreate.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.items.Item;
import de.undefinedhuman.projectcreate.engine.items.ItemType;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.utils.Utils;
import de.undefinedhuman.projectcreate.world.World;
import de.undefinedhuman.projectcreate.world.WorldManager;

import java.util.Arrays;
import java.util.HashMap;

public class ItemManager extends Manager {

    public static ItemManager instance;

    private HashMap<Integer, Item> items;

    public ItemManager() {
        if (instance == null) instance = this;
        this.items = new HashMap<>();
    }

    @Override
    public void init() {
        super.init();
        addItems(0, 1, 2);
    }

    public boolean addItems(int... ids) {
        boolean loaded = false;
        for (int id : ids) {
            if (hasItem(id) || !ResourceManager.existItem(id)) continue;
            items.put(id, loadItem(id));
            loaded = true;
        }
        if(loaded)
            Log.info("Item" + Utils.appendSToString(ids.length) + " loaded successfully: " + Arrays.toString(ids));
        return loaded;
    }

    public boolean hasItem(int id) {
        return items.containsKey(id);
    }

    public void removeItems(int... ids) {
        for(int id : ids) {
            if (!hasItem(id))
                continue;
            items.get(id).delete();
            items.remove(id);
        }
    }

    public Item getItem(int id) {
        if (hasItem(id) || addItems(id)) return items.get(id);
        return null;
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public void useItem(int id) {
        if(!InventoryManager.instance.canUseItem()) return;
        Item item = ItemManager.instance.getItem(id);
        if (item == null) return;
        boolean mouseLeft = Gdx.input.isButtonPressed(0), mouseRight = Gdx.input.isButtonPressed(1);
        switch (item.type) {
            case BLOCK:
                if (mouseLeft || mouseRight) WorldManager.instance.placeBlock(mouseLeft ? World.MAIN_LAYER : World.BACK_LAYER);
                break;
            case PICKAXE:
                if (mouseLeft || mouseRight) WorldManager.instance.destroyBlock(mouseLeft ? World.MAIN_LAYER : World.BACK_LAYER);
                break;
        }
    }

    @Override
    public void delete() {
        for (Item item : items.values()) item.delete();
        items.clear();
    }

    private Item loadItem(int id) {
        FileReader reader = new FsFile(Paths.ITEM_PATH, id + "/settings.item", Files.FileType.Internal).getFileReader(true);
        SettingsObject settingsObject = Utils.loadSettings(reader);
        if(!settingsObject.containsKey("Type")) return null;
        ItemType type = ItemType.valueOf(((LineSplitter) settingsObject.get("Type")).getNextString());
        Item item = type.createInstance();
        for(Setting setting : item.getSettings())
            setting.loadSetting(reader.getParentDirectory(), settingsObject);
        item.init();
        reader.close();
        return item;
    }

}
