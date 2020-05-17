package setting;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.*;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.Vector2Setting;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {

    private static SettingsList settingsList = new SettingsList();
    private static Setting
            stringSetting = new Setting(SettingType.String, "String key", ""),
            intSetting = new Setting(SettingType.Int, "Int key", 0),
            floatSetting = new Setting(SettingType.Float, "Float key", 0f),
            booleanSetting = new BooleanSetting("Boolean key", false),
            vector2Setting = new Vector2Setting("Vector2 key", new Vector2()),
            selectionSetting = new SelectionSetting("Selection key", new String[] { "Item 1", "Item 2", "Item 3" });

    private static FsFile settingsFile;

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();

        settingsList.addSettings(stringSetting, intSetting, floatSetting, booleanSetting, vector2Setting, selectionSetting);
        settingsFile = new FsFile("./test.settings", false);
    }

    @Test
    public void SettingsFile() {
        assertNotNull(settingsFile);
        Log.test("Settings file - PASSED");
    }

    @Test
    public void SettingsList() {
        assertNotNull(settingsList);
        assertFalse(settingsList.getSettings().isEmpty());
        Log.test("Settings list - PASSED");
    }

    @Test
    public void SettingsIO() {
        FileWriter settingsWriter = settingsFile.getFileWriter(false);
        stringSetting.setValue("Test Value");
        intSetting.setValue(1);
        floatSetting.setValue(1.5f);
        booleanSetting.setValue(true);
        vector2Setting.setValue(new Vector2(1, 1));
        ((SelectionSetting) selectionSetting).setSelected(1);

        Tools.saveSettings(settingsWriter, settingsList.getSettings());
        settingsWriter.close();

        assertFalse(settingsFile.isEmpty());
        resetSettings();

        FileReader settingsReader = settingsFile.getFileReader(false);
        Setting tempIntSetting = new Setting(SettingType.Int, "Second Int Key", 10);
        settingsList.addSettings(tempIntSetting);

        settingsReader.nextLine();
        HashMap<String, LineSplitter> settings = Tools.loadSettings(settingsReader);
        assertNotNull(settings);
        for(Setting setting : settingsList.getSettings()) setting.loadSetting(settingsReader.getParentDirectory(), settings);
        settingsReader.close();

        assertEquals("Test Value", stringSetting.getString());
        assertEquals(1, intSetting.getInt());
        assertEquals(1.5f, floatSetting.getFloat());
        assertTrue(booleanSetting.getBoolean());
        assertEquals(new Vector2(1, 1), vector2Setting.getVector2());
        assertEquals("Item 2", selectionSetting.getString());
        assertEquals(10, tempIntSetting.getInt());
        Log.test("Settings IO - PASSED");
    }

    @AfterAll
    static void afterAll() {
        FileUtils.deleteFile(settingsFile);
        Log.instance.save();
    }

    private void resetSettings() {
        stringSetting.setValue("");
        intSetting.setValue(0);
        floatSetting.setValue(0);
        booleanSetting.setValue(false);
        vector2Setting.setValue(new Vector2());
        ((SelectionSetting) selectionSetting).setSelected(0);
    }

}
