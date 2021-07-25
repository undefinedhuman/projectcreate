package de.undefinedhuman.projectcreate.editor.utils;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.util.Arrays;

public class Utils {

    public static void saveItem(int... ids) {
        int[] savedItems = Arrays.stream(ids).filter(id -> ItemManager.getInstance().hasItem(id)).peek(id -> {
            Item item = ItemManager.getInstance().getItem(id);
            FsFile itemDir = new FsFile(Paths.ITEM_PATH, item.getSettingsList().getSettings().get(0).getValue() + Variables.FILE_SEPARATOR, Files.FileType.Local);
            if(itemDir.exists())
                FileUtils.deleteFile(itemDir);

            FileWriter writer = new FsFile(itemDir, "settings.item", Files.FileType.Local).getFileWriter(true);
            writer.writeString("Type").writeString(item.type.name());
            writer.nextLine();
            Tools.saveSettings(writer, item.getSettingsList());
            writer.close();
        }).toArray();
        if(savedItems.length > 0)
            Log.debug("Item" + Tools.appendSToString(savedItems.length) + " " + Arrays.toString(savedItems) + " saved!");
        if(savedItems.length != ids.length)
            Log.error("Error while saving item" + Tools.appendSToString(ids.length - savedItems.length) + ": " + Arrays.toString(Arrays.stream(ids).filter(id -> Arrays.stream(savedItems).noneMatch(value -> value == id)).toArray()));
    }

    public static void saveBlueprints(int... ids) {
        int[] savedBlueprints = Arrays.stream(ids).filter(id -> BlueprintManager.getInstance().hasBlueprint(id)).peek(id -> {
            Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(id);
            FsFile entityDir = new FsFile(Paths.ENTITY_PATH, id + Variables.FILE_SEPARATOR, Files.FileType.Local);
            if(entityDir.exists())
                FileUtils.deleteFile(entityDir);

            FileWriter writer = new FsFile(entityDir, "settings.entity", Files.FileType.Local).getFileWriter(true);
            for(ComponentBlueprint componentBlueprint : blueprint.getComponentBlueprints().values())
                componentBlueprint.save(writer);
            writer.close();
        }).toArray();
        if(savedBlueprints.length > 0)
            Log.debug("Blueprint" + Tools.appendSToString(savedBlueprints.length) + " " + Arrays.toString(savedBlueprints) + " saved!");
        if(savedBlueprints.length != ids.length)
            Log.error("Error while saving blueprint" + Tools.appendSToString(ids.length - savedBlueprints.length) + ": " + Arrays.toString(Arrays.stream(ids).filter(id -> Arrays.stream(savedBlueprints).noneMatch(value -> value == id)).toArray()));
    }

    public static void addSettingsListToAccordion(SettingsGroup settingsGroup, Accordion accordion) {
        addSettingsListToAccordion(settingsGroup.getTitle(), settingsGroup, accordion);
    }

    public static void addSettingsListToAccordion(String title, SettingsList settingsList, Accordion accordion) {
        Accordion settingsAccordion = new Accordion(Variables.BACKGROUND_COLOR, false) {
            @Override
            public void update() {
                super.update();
                accordion.update();
            }
        };
        for(Setting<?> setting : settingsList.getSettings())
            setting.createSettingUI(settingsAccordion);
        accordion.addCollapsiblePanel(title, settingsAccordion);
    }

    public static void addComponentBlueprintsToAccordion(Accordion accordion, ComponentBlueprint... componentBlueprints) {
        for(ComponentBlueprint componentBlueprint : componentBlueprints)
            addSettingsListToAccordion(componentBlueprint.toString(), componentBlueprint, accordion);
    }

    public static int findSmallestMissing(Integer[] A, int left, int right) {
        if (left > right)
            return left;
        int mid = left + (right - left) / 2;
        if (A[mid] == mid) return findSmallestMissing(A, mid + 1, right);
        else return findSmallestMissing(A, left, mid - 1);
    }

}
