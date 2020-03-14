package de.undefinedhuman.sandboxgame.editor.entity.components.health;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class HealthComponent extends Component {

    private FloatSetting maxHealth;

    public HealthComponent(JPanel panel, String name) {

        super(panel, name);
        addSetting(new FloatSetting(panel,"MaxHealth",0,false));

    }

    @Override
    public void update() {

    }

}
