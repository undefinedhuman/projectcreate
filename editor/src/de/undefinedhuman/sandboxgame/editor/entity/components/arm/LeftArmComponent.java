package de.undefinedhuman.sandboxgame.editor.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class LeftArmComponent extends Component {

    public LeftArmComponent(JPanel panel, String name) {

        super(panel, name);
        addSetting(new StringSetting(panel, "Texture Data", "Temp", false));
        addSetting(new Vector2Setting(panel, "Turned Offset", new Vector2(), false));
        addSetting(new Vector2Setting(panel, "Origin", new Vector2(), false));

    }

    @Override
    public void update() { }


}
