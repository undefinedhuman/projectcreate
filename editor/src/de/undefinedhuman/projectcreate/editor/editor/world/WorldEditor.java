package de.undefinedhuman.projectcreate.editor.editor.world;

import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public class WorldEditor extends Editor {

    private static final int PREVIEW_PANEL_HEIGHT = 400 + Variables.BORDER_HEIGHT;
    private static final int BASE_OFFSET_X = 20;
    private static final int BASE_OFFSET_Y = 15;
    private static final int SETTINGS_PANEL_WIDTH = 400;

    private JPanel previewPanel, settingsPanel;
    private JLabel previewChunkGeneration;

    private Preview preview;
    private Thread previewGenerator;

    public WorldEditor(Container container, int width, int height) {
        super();
        int previewPanelWidth = width - BASE_OFFSET_X*2 - SETTINGS_PANEL_WIDTH - Variables.BORDER_WIDTH;
        /*addPanel(
                container,
                createScrollPane(
                        settingsPanel = createJPanel(SETTINGS_PANEL_WIDTH, height - BASE_OFFSET_Y*2 - Variables.BORDER_HEIGHT - Variables.OFFSET),
                        "Settings:",
                        BASE_OFFSET_X,
                        BASE_OFFSET_Y,
                        SETTINGS_PANEL_WIDTH + Variables.BORDER_WIDTH,
                        height - BASE_OFFSET_Y*2,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                createScrollPane(
                        previewPanel = createJPanel(previewPanelWidth, PREVIEW_PANEL_HEIGHT),
                        "Preview:",
                        BASE_OFFSET_X + SETTINGS_PANEL_WIDTH + Variables.BORDER_WIDTH + Variables.OFFSET,
                        BASE_OFFSET_Y,
                        previewPanelWidth + Variables.BORDER_WIDTH,
                        PREVIEW_PANEL_HEIGHT,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        );
        previewChunkGeneration = new JLabel();
        previewChunkGeneration.setBounds(0, 0, previewPanel.getWidth(), previewPanel.getHeight());
        NoisePanel panel = new NoisePanel("Noise", settingsPanel.getHeight(), ConstantFunction.class, FractalFunction.class, GradientFunction.class, ScaleFunction.class, SelectFunction.class) {
            @Override
            public void updateValues() {
                updatePreview();
            }
        };

        preview = new Preview(previewChunkGeneration, panel);

        updatePreview();

        Tools.createSettingsPanel(settingsPanel, panel);

        previewPanel.add(previewChunkGeneration);*/
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
    public void init() {

    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }

    @Override
    public void createMenuButtonsPanel(JPanel menuButtonPanel) {

    }

}
