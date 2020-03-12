package de.undefinedhuman.sandboxgame.editor.entity.components.collision;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class CollisionComponent extends Component {

    private JPanel panel;

    public CollisionComponent(JPanel panel, String name) {

        super(panel, name);
        addSetting(new FloatSetting(panel,"Width",0, false));
        addSetting(new FloatSetting(panel,"Height",0, false));
        addSetting(new Vector2Setting(panel,"Offset", new Vector2(0,0), false));

    }

    @Override
    public void update() { }

}
