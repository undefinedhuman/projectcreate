package de.undefinedhuman.projectcreate.editor.editor.utils;

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
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.util.Locale;

public class Utils {

    public static void saveItem(int id) {
        if(!ItemManager.getInstance().hasItem(id))
            return;
        Item item = ItemManager.getInstance().getItem(id);
        FsFile itemDir = new FsFile(Paths.ITEM_PATH, item.getSettingsList().getSettings().get(0).getValue() + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(itemDir.exists())
            FileUtils.deleteFile(itemDir);

        FileWriter writer = new FsFile(itemDir, "settings.item", Files.FileType.Local).getFileWriter(true);
        writer.writeString("Type").writeString(item.type.name());
        writer.nextLine();
        Tools.saveSettings(writer, item.getSettingsList());
        writer.close();
        Log.debug("Item " + id + " saved!");
    }

    public static void saveBlueprint(int id) {
        if(!BlueprintManager.getInstance().hasBlueprint(id))
            return;
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(id);
        FsFile entityDir = new FsFile(Paths.ENTITY_PATH, id + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(entityDir.exists())
            FileUtils.deleteFile(entityDir);

        FileWriter writer = new FsFile(entityDir, "settings.entity", Files.FileType.Local).getFileWriter(true);
        for(ComponentBlueprint componentBlueprint : blueprint.getComponentBlueprints().values())
            componentBlueprint.save(writer);
        writer.close();
        Log.debug("Blueprint " + id + " saved!");
    }

    public static void addSettingsGroupToAccordion(SettingsGroup settingsGroup, Accordion accordion) {
        Accordion settingsAccordion = new Accordion(Variables.BACKGROUND_COLOR.darker()) {
            @Override
            public void update() {
                super.update();
                accordion.update();
            }
        };
        for(Setting<?> setting : settingsGroup.getSettings())
            setting.createSettingUI(settingsAccordion);
        accordion.addCollapsiblePanel(settingsGroup.getTitle(), settingsAccordion);
    }

    public static void addSettingsGroupToAccordion(ComponentBlueprint componentBlueprint, Accordion accordion) {
        for(Setting<?> setting : componentBlueprint.getSettings())
            setting.createSettingUI(accordion);
    }

    public static String getComponentBlueprintName(ComponentBlueprint componentBlueprint) {
        String name = componentBlueprint.getClass().getSimpleName().split("Blueprint")[0];
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1).toLowerCase();
    }

    public static int findSmallestMissing(Integer[] A, int left, int right) {
        if (left > right)
            return left;
        int mid = left + (right - left) / 2;
        if (A[mid] == mid) return findSmallestMissing(A, mid + 1, right);
        else return findSmallestMissing(A, left, mid - 1);
    }

}
