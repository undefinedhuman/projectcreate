package de.undefinedhuman.projectcreate.engine.settings;

import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class SettingsObjectAdapter extends SettingsObject {

    public SettingsObjectAdapter(FileReader reader) {
        while(reader.nextLine() != null) {
            String key = reader.getNextString();
            if(key.startsWith("}")) return;
            else if(key.startsWith("{")) addSetting(loadKey(key), new SettingsObjectAdapter(reader));
            else addSetting(key, reader.getRemainingRawData());
        }
    }

    private static String loadKey(String key) {
        String[] values = key.split(":");
        if(values.length < 2)
            Log.showErrorDialog("Can't find key in string: " + key, true);
        return values[1];
    }

}
