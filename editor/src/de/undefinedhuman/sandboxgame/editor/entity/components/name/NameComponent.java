package de.undefinedhuman.sandboxgame.editor.entity.components.name;

import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class NameComponent extends Component {

    private StringSetting nameSetting;

    public NameComponent(JPanel panel, String name) {

        super(panel, name);

        addSetting(new StringSetting(panel,"Name","",false));

    }

    @Override
    public void update() { }

}
