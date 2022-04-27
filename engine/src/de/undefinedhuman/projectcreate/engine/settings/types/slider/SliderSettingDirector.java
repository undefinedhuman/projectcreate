package de.undefinedhuman.projectcreate.engine.settings.types.slider;

public class SliderSettingDirector {

    public static SliderSetting.Builder createIntegerSlider(SliderSetting.Builder builder) {
        builder.tickSpeed = 1;
        builder.numberOfDecimals = 0;
        builder.scale = 1f;
        return builder;
    }

    public static SliderSetting.Builder createFloatSlider(SliderSetting.Builder builder) {
        builder.tickSpeed = 1;
        builder.numberOfDecimals = 2;
        builder.scale = 100f;
        return builder;
    }

}
