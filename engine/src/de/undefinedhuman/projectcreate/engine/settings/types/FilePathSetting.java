package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class FilePathSetting extends Setting {

    private FsFile defaultFile;

    public FilePathSetting(String key, FsFile defaultFile) {
        super(SettingType.FilePath, key, defaultFile.file().getAbsolutePath());
        this.defaultFile = defaultFile;
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        super.addValueMenuComponents(panel, position);
        valueField.setEditable(false);
        valueField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setValue(chooseFilePath(defaultFile));
            }
        });
    }

    public abstract String chooseFilePath(FsFile defaultFile);

}
