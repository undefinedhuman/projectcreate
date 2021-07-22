package de.undefinedhuman.projectcreate.core.items;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectAdapter;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

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

    public boolean loadItems(int... ids) {
        int[] loadedItemIDs = Arrays.stream(ids)
                .filter(id -> !hasItem(id) && RessourceUtils.existItem(id))
                .peek(id -> {
                    Item item = loadItem(id);
                    addItem(id, item);
                })
                .filter(this::hasItem)
                .toArray();
        int[] failedItemIDs = Arrays.stream(ids).filter(id -> {
            if(hasItem(id))
                return false;
            for (int loadedItemID : loadedItemIDs)
                if (loadedItemID == id) return false;
            return true;
        }).toArray();
        if(failedItemIDs.length > 0)
            Log.error("Error while loading item" + Tools.appendSToString(failedItemIDs.length) + ": " + Arrays.toString(Arrays.stream(failedItemIDs).sorted().toArray()));
        if(loadedItemIDs.length > 0)
            Log.debug("Item" + Tools.appendSToString(loadedItemIDs.length) + " loaded: " + Arrays.toString(Arrays.stream(loadedItemIDs).sorted().toArray()));
        return loadedItemIDs.length == ids.length;
    }

    public void addItem(int id, Item item) {
        if(hasItem(id))
            return;
        items.put(id, item);
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

    @Override
    public void delete() {
        for (Item item : items.values())
            item.delete();
        items.clear();
    }

    private Item loadItem(int id) {
        FileReader reader = new FsFile(Paths.ITEM_PATH, id + "/settings.item", Files.FileType.Internal).getFileReader(true);
        SettingsObject settingsObject = new SettingsObjectAdapter(reader);
        ItemType type;
        if(!settingsObject.containsKey("Type"))
            type = ItemType.ITEM;
        else {
            String typeName = ((LineSplitter) settingsObject.get("Type")).getNextString();
            if(typeName == null || typeName.equals("")) type = ItemType.ITEM;
            else type = ItemType.valueOf(typeName);
        }
        Item item = type.createInstance();
        for(Setting<?> setting : item.getSettingsList().getSettings())
            setting.load(reader.parent(), settingsObject);
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
