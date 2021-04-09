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
    private int tickSpeed;
    private float conversionValue;

    public SliderSetting(String key, int min, int max, int tickSpeed) {
        this(key, min, max, min, tickSpeed, 1f);
    }

    public SliderSetting(String key, int min, int max, int defaultValue, int tickSpeed, float conversionValue) {
        super(SettingType.Selection, key, defaultValue / conversionValue);
        this.bounds.set(min, max);
        this.tickSpeed = tickSpeed;
        this.includeType = false;
        this.conversionValue = conversionValue;
    }

    @Override
    protected void delete() {
        super.delete();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        JLabel progressLabel = new JLabel(getString());
        progressLabel.setBounds((int) position.x + 150, (int) position.y, 50, 25);
        slider = new JSlider(bounds.x, bounds.y, (int) (getFloat() * conversionValue));
        slider.setMajorTickSpacing(tickSpeed);
        slider.setSnapToTicks(true);
        slider.setBounds((int) position.x, (int) position.y, 150, 25);
        slider.addChangeListener(e -> {
            double value = Math.floor((slider.getValue() / conversionValue) * 100) / 100;
            setValue(value);
            progressLabel.setText(String.valueOf(value));
        });
        panel.add(slider);
        panel.add(progressLabel);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(slider == null)
            return;
        if(!Tools.isFloat(value.toString())) {
            Log.error("Error while setting value of setting (float conversion error): " + key);
            return;
        }
        float valueParsed = Float.parseFloat(value.toString());
        slider.setValue(Tools.isInRange(valueParsed, bounds.x, bounds.y) ? (int) (valueParsed * conversionValue) : bounds.x);
    }

}
