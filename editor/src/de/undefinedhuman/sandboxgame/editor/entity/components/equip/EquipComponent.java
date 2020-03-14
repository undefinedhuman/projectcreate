package de.undefinedhuman.sandboxgame.editor.entity.components.equip;

import me.gentlexd.sandboxeditor.editor.settings.PositionSetting;
import me.gentlexd.sandboxeditor.editor.settings.StringArraySetting;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.swing.*;

public class EquipComponent extends Component {

    public EquipComponent(JPanel panel, String name) {

        super(panel, name);

        addSetting(new StringSetting(panel,"Item Texture","Temp",false));
        addSetting(new StringSetting(panel,"Item Hitbox","Temp",false));
        addSetting(new StringSetting(panel,"Right Arm","Temp",false));
        addSetting(new PositionSetting(panel,"Item Position","",false,false));
        addSetting(new PositionSetting(panel, "Item Offset","",true,false));
        addSetting(new StringArraySetting(panel,"Invisible Sprites","",false));

    }

    @Override
    public void update() { }

}
