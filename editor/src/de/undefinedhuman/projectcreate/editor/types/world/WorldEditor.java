package de.undefinedhuman.projectcreate.editor.types.world;

import de.undefinedhuman.projectcreate.core.noise.functions.NoisePanel;
import de.undefinedhuman.projectcreate.core.noise.functions.fractal.FractalFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.types.ConstantFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.types.GradientFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.types.ScaleFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.types.SelectFunction;
import de.undefinedhuman.projectcreate.editor.types.Editor;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class WorldEditor extends Editor {

    private Preview preview;
    private Thread previewGenerator;

    public WorldEditor() {
        super();
        NoisePanel noisePanel = new NoisePanel("Noise", ConstantFunction.class, FractalFunction.class, GradientFunction.class, ScaleFunction.class, SelectFunction.class) {
            @Override
            public void updateValues() {
                updatePreview();
            }
        };

        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));
        panel.add(SettingsUtils.setPreferredSize(0, 400, preview = new Preview(noisePanel)));
        add(panel, 0.75f);

        Accordion accordion;
        add(accordion = new Accordion(Variables.BACKGROUND_COLOR), 0.25f);
        noisePanel.createSettingUI(accordion);
    }

    private void updatePreview() {
        if (previewGenerator != null && previewGenerator.isAlive()) {
            previewGenerator.interrupt();
            previewGenerator.stop();
        }
        previewGenerator = new Thread(preview);
        previewGenerator.start();
    }

    @Override
    public void createMenuButtonsPanel(JPanel menuButtonPanel) {}

}
