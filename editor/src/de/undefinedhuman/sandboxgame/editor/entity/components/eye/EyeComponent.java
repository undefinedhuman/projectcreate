package de.undefinedhuman.sandboxgame.editor.entity.components.eye;

import me.gentlexd.sandboxeditor.editor.settings.FloatSetting;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class EyeComponent extends Component {

    private StringSetting textureData;
    private FloatSetting scaleHeight;

    public EyeComponent(JPanel panel, String name) {

        super(panel, name);

        addSetting(new StringSetting(panel,"Texture Data","",false));
        addSetting(new FloatSetting(panel,"Scale Height",0,false));

    }

    @Override
    public void update() {

    }

}
