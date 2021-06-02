package de.undefinedhuman.projectcreate.editor.editor.world;

import de.undefinedhuman.projectcreate.core.noise.functions.FractalFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.GradientFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.NoisePanel;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.LongSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSettingDirector;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WorldEditor extends Editor {

    private static final int CHUNK_HEIGHT = 400;
    private JPanel previewPanel, settingsPanel;
    private JLabel previewChunkGeneration;

    private LongSetting seed;
    private SliderSetting frequency, octaves, lacunarity, amplitudeFactor, gain;

    private Preview preview;
    private Thread previewGenerator;

    public WorldEditor(Container container) {
        super(container);
        addPanel(
                container,
                createScrollPane(previewPanel = createJPanel(1880, CHUNK_HEIGHT + 40), "Preview:", 20, 15, 1880, CHUNK_HEIGHT + Variables.BORDER_HEIGHT + 40, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                createScrollPane(settingsPanel = createJPanel(1880, CHUNK_HEIGHT + 40), "Settings:", 20, 15 + CHUNK_HEIGHT + Variables.BORDER_HEIGHT + 40, 1880, 990 - CHUNK_HEIGHT - 40 - Variables.BORDER_HEIGHT, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        );
        previewChunkGeneration = new JLabel();
        previewChunkGeneration.setBounds(20, 15, 1880 - Variables.BORDER_WIDTH - 40, CHUNK_HEIGHT);
        preview = new Preview(previewChunkGeneration);

        seed = new LongSetting("Seed", Math.abs(new Random().nextLong()), 0L, Long.MAX_VALUE-1) {
            @Override
            public void setValue(Object value) {
                super.setValue(value);
                updatePreview();
            }
        };
        seed.addValueListener(value -> updatePreview());

        frequency = SliderSetting.newInstance("Frequency")
                .with(builder -> {
                    builder.bounds.set(10, 1500);
                    builder.defaultValue = 1000;
                    builder.tickSpeed = 5;
                    builder.scale = 100000;
                    builder.numberOfDecimals = 4;
                })
                .build();
        frequency.addValueListener(value -> updatePreview());

        octaves = SliderSettingDirector.createIntegerSlider(SliderSetting.newInstance("Octaves"))
                .with(builder -> {
                    builder.bounds.set(1, 10);
                    builder.defaultValue = 3;
                })
                .build();
        octaves.addValueListener(value -> updatePreview());

        lacunarity = SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Lacunarity"))
                .with(builder -> {
                    builder.bounds.set(0, 400);
                    builder.defaultValue = 200;
                })
                .build();
        lacunarity.addValueListener(value -> updatePreview());

        amplitudeFactor = SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Amplitude Factor"))
                .with(builder -> {
                    builder.bounds.set(0, 100);
                    builder.tickSpeed = 1;
                })
                .build();
        amplitudeFactor.addValueListener(value -> updatePreview());

        gain = SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Gain"))
                .with(builder -> {
                    builder.defaultValue = 50;
                })
                .build();
        gain.addValueListener(value -> updatePreview());



        updatePreview();

        NoisePanel panel = new NoisePanel("Noise", FractalFunction.class, GradientFunction.class);

        Tools.addSettings(settingsPanel, panel);

        // Tools.addSettings(settingsPanel, Stream.of(seed, frequency, octaves, lacunarity, gain, amplitudeFactor));

        previewPanel.add(previewChunkGeneration);
    }

    private void updatePreview() {
        preview.setSeed(seed.getValue());
        preview.setFrequency(frequency.getValue());
        preview.setOctaves(octaves.getValue());
        preview.setLacunarity(lacunarity.getValue());
        preview.setGain(gain.getValue());
        preview.setAmplitudeFactor(amplitudeFactor.getValue());
        if (previewGenerator != null && previewGenerator.isAlive()) {
            previewGenerator.interrupt();
            previewGenerator.stop();
        }
        previewGenerator = new Thread(preview);
        previewGenerator.start();
    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }

}
