package de.undefinedhuman.sandboxgame.item;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.resources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsObject;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;
import de.undefinedhuman.sandboxgame.world.WorldManager;

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
            Log.info("Item" + Tools.appendSToString(ids.length) + " loaded successfully: " + Arrays.toString(ids));
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
        FileReader reader = new FsFile(Paths.ITEM_PATH, id + "/settings.item", false).getFileReader(true);
        SettingsObject settingsObject = Tools.loadSettings(reader);
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
