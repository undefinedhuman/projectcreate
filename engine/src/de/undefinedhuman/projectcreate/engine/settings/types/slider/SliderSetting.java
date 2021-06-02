package de.undefinedhuman.projectcreate.engine.settings.types.slider;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SliderSetting extends Setting<Float> {

    private final float sliderWidth;
    private final boolean isLabelVisible;
    private final Vector2i bounds = new Vector2i();
    private final int tickSpeed;
    private final float scale;
    private final int numberOfDecimals;

    private JSlider slider;
    private JLabel progressLabel;

    private SliderSetting(String key, float sliderWidth, boolean isLabelVisible, Vector2i bounds, int defaultValue, int tickSpeed, float scale, int numberOfDecimals) {
        super(key, defaultValue / scale, value -> Float.parseFloat(String.valueOf(value)));
        this.sliderWidth = sliderWidth;
        this.isLabelVisible = isLabelVisible;
        this.bounds.set(bounds);
        this.tickSpeed = tickSpeed;
        this.scale = scale;
        this.numberOfDecimals = numberOfDecimals;
        setMenuTitle(key + ": Range [" + bounds.x / scale + ", " + bounds.y / scale + "]");
    }

    @Override
    protected void delete() {
        super.delete();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        slider = createSliderUI(width, getContentHeight(), sliderWidth, bounds, getValue(), scale, tickSpeed, e -> setValue(convertToPreciseFloatingPointValue(slider.getValue(), scale, numberOfDecimals)));
        panel.add(slider);
        if(isLabelVisible) {
            progressLabel = createSliderLabelUI(width, getContentHeight(), sliderWidth);
            addValueListener(value -> progressLabel.setText(String.valueOf(value)));
            panel.add(progressLabel);
        }
    }

    private double convertToPreciseFloatingPointValue(int value, float scale, int numberOfDecimals) {
        double precision = Math.pow(10, numberOfDecimals);
        double floatValue = value / scale;
        return Math.floor(floatValue * precision) / precision;
    }

    private JSlider createSliderUI(int contentWidth, int contentHeight, float sliderWidth, Vector2i bounds, float currentValue, float scale, int tickSpeed, ChangeListener onChange) {
        JSlider slider = new JSlider(bounds.x, bounds.y, (int) (currentValue * scale));
        slider.setMajorTickSpacing(tickSpeed);
        slider.setBounds(0, 0, (int) (contentWidth*sliderWidth), contentHeight);
        slider.addChangeListener(onChange);
        return slider;
    }

    private JLabel createSliderLabelUI(int contentWidth, int contentHeight, float sliderWidth) {
        JLabel progressLabel = new JLabel(String.valueOf(getValue()));
        progressLabel.setBounds((int) (contentWidth*sliderWidth), 0, (int) (contentWidth*(1f-sliderWidth)), contentHeight);
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return progressLabel;
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(slider == null)
            return;
        Float parsedValue = Tools.isFloat(value.toString());
        if(parsedValue == null) {
            Log.error("Error while loading value of setting (float conversion error): " + value);
            return;
        }
        slider.setValue(Tools.isInRange(parsedValue, bounds.x, bounds.y) ? (int) (parsedValue * scale) : (int) (bounds.x * scale));
    }

    public static Builder newInstance(String key) {
        return new Builder(key);
    }

    public static class Builder {

        public String key;
        public Vector2i bounds = new Vector2i(0, 100);
        public boolean isLabelVisible = true;
        public float relativeWidth = 0.85f;
        public int defaultValue = 0;
        public int tickSpeed = 1;
        public float scale = 1f;
        public int numberOfDecimals = 2;

        private Builder(String key) {
            this.key = key;
        }

        public Builder with(Consumer<Builder> function) {
            function.accept(this);
            return this;
        }

        public SliderSetting build() {
            validate();
            return new SliderSetting(key, relativeWidth, isLabelVisible, bounds, defaultValue, tickSpeed, scale, numberOfDecimals);
        }

        private static List<ValidationRule<Builder>> VALIDATION_RULES = Arrays.asList(
                new ValidationRule<>(
                        builder -> builder.bounds.x <= builder.bounds.y,
                        builder -> "Builder (" + builder.key + "): Slider lower bounds must be smaller or equal to upper",
                        builder -> builder.bounds.set(builder.bounds.y, builder.bounds.y)
                ),
                new ValidationRule<>(
                        builder -> builder.numberOfDecimals >= 0,
                        builder -> "Builder (" + builder.key + "): Number of decimals must be larger then or equal to 0",
                        builder -> builder.numberOfDecimals = 0
                ),
                new ValidationRule<>(
                        builder -> Tools.isInRange(builder.relativeWidth, 0, 1f),
                        builder -> "Builder (" + builder.key + "): Relative Slider UI width must be between 0f and 1f",
                        builder -> builder.relativeWidth = 0.85f
                ),
                new ValidationRule<>(
                        builder -> builder.tickSpeed <= builder.bounds.y - builder.bounds.x,
                        builder -> "Builder (" + builder.key + "): Slider tick speed may not exceed the slider boundaries",
                        builder -> builder.tickSpeed = (builder.bounds.y - builder.bounds.x)/2
                ),
                new ValidationRule<>(
                        builder -> builder.isLabelVisible && builder.relativeWidth <= 0.95f,
                        builder -> "Builder (" + builder.key + "): If label shall be visible, the slider width should not exceed 0.95f",
                        builder -> builder.relativeWidth = 0.95f
                ),
                new ValidationRule<>(
                        builder -> Tools.isInRange(builder.defaultValue, builder.bounds.x, builder.bounds.y),
                        builder -> "Builder (" + builder.key + "): The default value of the slider should be between boundaries",
                        builder -> builder.defaultValue = builder.bounds.x
                ),
                new ValidationRule<>(
                        builder -> builder.scale > 0,
                        builder -> "Builder (" + builder.key + "): Value scale cannot be less than or equal to zero",
                        builder -> builder.scale = 1f
                )
        );

        private void validate() {
            for(ValidationRule<Builder> validationRule : VALIDATION_RULES) {
                if(validationRule.isValid(this))
                    continue;
                Log.error(validationRule.getErrorMessage(this));
                validationRule.correct(this);
            }
        }
    }

}
