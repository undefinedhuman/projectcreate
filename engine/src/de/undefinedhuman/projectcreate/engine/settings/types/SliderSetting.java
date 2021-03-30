package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;

public class SliderSetting extends Setting {

    public JSlider slider;
    private Vector2i bounds = new Vector2i();
    private int extent;

    public SliderSetting(String key, int min, int max, int value, int extent) {
        super(SettingType.Selection, key, value);
        this.bounds.set(min, max);
        this.extent = extent;
    }

    @Override
    protected void delete() {
        super.delete();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        slider = new JSlider(bounds.x, bounds.y, getInt());
        slider.setExtent(extent);
        slider.setBounds((int) position.x, (int) position.y, 200, 25);
        slider.addChangeListener(e -> setValue(slider.getValue()));
        panel.add(slider);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(slider == null || !Tools.isDigit(value.toString())) {
            Log.error("Error while setting value of setting (Value isn't an Integer or UI does not exist): " + key);
            return;
        }
        int valueParsed = Integer.parseInt(value.toString());
        slider.setValue(Tools.isInRange(valueParsed, bounds.x, bounds.y) ? valueParsed : bounds.x);
    }

}
