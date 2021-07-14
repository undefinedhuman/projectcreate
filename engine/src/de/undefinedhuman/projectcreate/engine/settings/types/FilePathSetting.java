package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class FilePathSetting extends TextFieldSetting<FsFile> {

    private FsFile defaultFile;

    public FilePathSetting(String key, FsFile defaultFile) {
        super(key, defaultFile, value -> new FsFile(value, Files.FileType.Absolute), value -> value.file().getAbsolutePath());
        this.defaultFile = defaultFile;
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        super.createSettingUI(accordion);
        valueField.setEditable(false);
        valueField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setValue(chooseFilePath(defaultFile));
            }
        });
    }

    public abstract FsFile chooseFilePath(FsFile defaultFile);

}
