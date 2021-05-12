package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class FilePathSetting extends Setting {

    private FsFile defaultFile;

    public FilePathSetting(String key, FsFile defaultFile) {
        super(key, defaultFile.file().getAbsolutePath());
        this.defaultFile = defaultFile;
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        super.addValueMenuComponents(panel, width);
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
