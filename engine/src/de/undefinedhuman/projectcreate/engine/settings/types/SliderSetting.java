package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;

public class SliderSetting extends Setting<Float> {

    public JSlider slider;
    private Vector2i bounds = new Vector2i();
    private int tickSpeed;
    private float conversionValue;

    public SliderSetting(String key, int min, int max, int tickSpeed) {
        this(key, min, max, min, tickSpeed, 1f);
    }

    public SliderSetting(String key, int min, int max, int defaultValue, int tickSpeed, float conversionValue) {
        super(key, defaultValue / conversionValue, value -> Float.parseFloat(String.valueOf(value)));
        this.bounds.set(min, max);
        this.tickSpeed = tickSpeed;
        this.conversionValue = conversionValue;
        setMenuTitle(key + ": Range [" + min + ", " + max + "]");
    }

    @Override
    protected void delete() {
        super.delete();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        JLabel progressLabel = new JLabel(String.valueOf(getValue()));
        progressLabel.setBounds((int) (width*0.75f), 0, (int) (width*0.25f), Variables.DEFAULT_CONTENT_HEIGHT);
        slider = new JSlider(bounds.x, bounds.y, (int) (getValue() * conversionValue));
        slider.setMajorTickSpacing(tickSpeed);
        slider.setSnapToTicks(true);
        slider.setBounds(0, 0, (int) (width*0.75f), Variables.DEFAULT_CONTENT_HEIGHT);
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
        if(Tools.isFloat(value.toString()) == null) {
            Log.error("Error while setting value of setting (float conversion error): " + key);
            return;
        }
        float valueParsed = Float.parseFloat(value.toString());
        slider.setValue(Tools.isInRange(valueParsed, bounds.x, bounds.y) ? (int) (valueParsed * conversionValue) : (int) (bounds.x * conversionValue));
    }

}
