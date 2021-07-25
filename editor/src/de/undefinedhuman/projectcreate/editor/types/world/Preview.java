package de.undefinedhuman.projectcreate.editor.types.world;

import de.undefinedhuman.projectcreate.core.noise.functions.NoisePanel;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Preview implements Runnable {

    private JLabel previewTextureLabel;
    private BufferedImage noiseImage;
    private NoisePanel noisePanel;

    public Preview(JLabel previewTextureLabel, NoisePanel noisePanel) {
        this.previewTextureLabel = previewTextureLabel;
        this.noiseImage = new BufferedImage(previewTextureLabel.getWidth(), previewTextureLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        previewTextureLabel.setIcon(new ImageIcon(noiseImage));
        this.noisePanel = noisePanel;
    }

    @Override
    public void run() {

        long currentMillis = System.currentTimeMillis();

        for(int x = 0; x < noiseImage.getWidth(); x++)
            for(int y = 0; y < noiseImage.getHeight(); y++) {
                double noiseValue = Tools.clamp((float) noisePanel.calculateValue(x, y), 0f, 1f);
                noiseImage.setRGB(x, y, new Color((int) (noiseValue * 255), (int) (noiseValue * 255), (int) (noiseValue * 255)).getRGB());
            }

        Log.info(System.currentTimeMillis() - currentMillis);

        previewTextureLabel.revalidate();
        previewTextureLabel.repaint();
    }

}
