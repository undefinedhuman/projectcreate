package de.undefinedhuman.sandboxgame.item;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.resources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.utils.Tools;
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

    @Override
    public void delete() {
        for (Item item : items.values()) item.delete();
        items.clear();
    }

    public void removeItems(int... id) {
        for (int i : id) removeItem(i);
    }

    public void removeItem(int id) {
        if (hasItem(id)) {
            items.get(id).delete();
            items.remove(id);
        }
    }

    public Item getItem(int id) {
        if (hasItem(id)) return items.get(id);
        else if (addItems(id)) return items.get(id);
        return hasItem(0) ? getItem(0) : null;
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
                if (mouseLeft || mouseRight) WorldManager.instance.placeBlock(mouseLeft);
                break;
            case PICKAXE:
                if (mouseLeft || mouseRight) WorldManager.instance.destroyBlock(mouseLeft);
                break;
        }
    }

    private Item loadItem(int id) {
        FileReader reader = new FileReader(ResourceManager.loadFile(Paths.ITEM_PATH, id + "/settings.item"), true);
        reader.nextLine();
        ItemType type = ItemType.valueOf(reader.getNextString());
        reader.nextLine();
        HashMap<String, LineSplitter> settings = Tools.loadSettings(reader);
        Item item = type.createInstance();
        if(item == null) return null;
        for(Setting setting : item.getSettings()) setting.loadSetting(reader.getParentDirectory(), settings);
        item.init();
        reader.close();
        settings.clear();
        return item;
    }

}
