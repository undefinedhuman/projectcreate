package de.undefinedhuman.sandboxgame.editor.entity.components.effect;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.IntSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class RegenerationComponent extends Component {

    public RegenerationComponent(JPanel panel, String name) {

        super(panel, name);
        addSetting(new FloatSetting(panel,"Reg Delay",0, false));
        addSetting(new IntSetting(panel,"Reg. Amount",0, false));

    }

    @Override
    public void update() { }

}
