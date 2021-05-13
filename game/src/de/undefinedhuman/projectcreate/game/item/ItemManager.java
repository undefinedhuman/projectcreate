package de.undefinedhuman.projectcreate.game.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.WorldManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemManager extends Manager {

    private static volatile ItemManager instance;

    private HashMap<Integer, Item> items;

    private ItemManager() {
        this.items = new HashMap<>();
    }

    @Override
    public void init() {
        super.init();
        loadItems(0, 1, 2);
    }

    public boolean loadItems(int... ids) {
        Arrays.stream(ids)
                .filter(id -> !hasItem(id) && ResourceManager.existItem(id))
                .forEach(id -> items.put(id, loadItem(id)));
        Object[] loadedItems = Arrays.stream(ids)
                .filter(id -> items.containsKey(id) && items.get(id) != null)
                .mapToObj(Integer::toString)
                .toArray();
        Log.debug(() -> {
            if(loadedItems.length == 0)
                return "";
            return "Item" + Tools.appendSToString(loadedItems.length) + " loaded: " + Arrays.toString(loadedItems);
        });
        return loadedItems.length == ids.length;
    }

    public boolean hasItem(int id) {
        return items.containsKey(id);
    }

    public void removeItems(int... ids) {
        Stream<String> removedItems = Arrays.stream(ids)
                .filter(this::hasItem)
                .mapToObj(id -> {
                    items.get(id).delete();
                    items.remove(id);
                    return Integer.toString(id);
                });
        Log.debug(() -> "Item" + Tools.appendSToString(ids.length) + " unloaded: " + removedItems.collect(Collectors.joining(", ")));
    }

    public Item getItem(int id) {
        if (hasItem(id) || loadItems(id))
            return items.get(id);
        return null;
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public void useItem(int id) {
        if(!InventoryManager.getInstance().canUseItem()) return;
        Item item = ItemManager.instance.getItem(id);
        if (item == null) return;
        boolean mouseLeft = Gdx.input.isButtonPressed(0), mouseRight = Gdx.input.isButtonPressed(1);
        switch (item.type) {
            case BLOCK:
                if (mouseLeft || mouseRight) WorldManager.getInstance().placeBlock(mouseLeft ? World.MAIN_LAYER : World.BACK_LAYER);
                break;
            case PICKAXE:
                if (mouseLeft || mouseRight) WorldManager.getInstance().destroyBlock(mouseLeft ? World.MAIN_LAYER : World.BACK_LAYER);
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
        SettingsObject settingsObject = Tools.loadSettings(reader);
        if(!settingsObject.containsKey("Type")) return null;
        ItemType type = ItemType.valueOf(((LineSplitter) settingsObject.get("Type")).getNextString());
        Item item = type.createInstance();
        for(Setting<?> setting : item.getSettingsList().getSettings())
            setting.loadSetting(reader.parent(), settingsObject);
        item.init();
        reader.close();
        return item;
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            synchronized (ItemManager.class) {
                if (instance == null)
                    instance = new ItemManager();
            }
        }
        return instance;
    }

}
