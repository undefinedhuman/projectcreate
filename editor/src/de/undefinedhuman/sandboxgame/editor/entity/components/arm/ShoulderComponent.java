package de.undefinedhuman.sandboxgame.editor.entity.components.arm;

import me.gentlexd.sandboxeditor.editor.settings.PositionSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class ShoulderComponent extends Component {

    public ShoulderComponent(JPanel panel, String name) {

        super(panel, name);

        addSetting(new PositionSetting(panel,"Shoulder Pos","",false,false));
        addSetting(new PositionSetting(panel, "Shoulder Off", "",true,false));

    }

    @Override
    public void update() { }

}
