package de.undefinedhuman.sandboxgame.editor.entity.components.movement;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class MovementComponent extends Component {

    public MovementComponent(JPanel panel, String name) {

        super(panel, name);
        this.panel = panel;

        addSetting(new FloatSetting(panel,"Speed",0, false));
        addSetting(new FloatSetting(panel,"Jump-Speed",0, false));
        addSetting(new FloatSetting(panel,"Gravity",0, false));

    }

    @Override
    public void update() { }

}
