package de.undefinedhuman.projectcreate.editor.types.world;

import de.undefinedhuman.projectcreate.core.noise.functions.NoisePanel;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Preview extends JLabel implements Runnable {

    private NoisePanel noisePanel;

    public Preview(NoisePanel noisePanel) {
        this.noisePanel = noisePanel;
    }

    @Override
    public void run() {
        if(getWidth() == 0 || getHeight() == 0)
            return;

        BufferedImage noiseImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        if(!Thread.interrupted())
            setIcon(new ImageIcon(noiseImage));

        for(int x = 0; x < noiseImage.getWidth(); x++)
            for(int y = 0; y < noiseImage.getHeight(); y++) {
                double noiseValue = Tools.clamp((float) noisePanel.calculateValue(x, y), 0f, 1f);
                noiseImage.setRGB(x, y, new Color((int) (noiseValue * 255), (int) (noiseValue * 255), (int) (noiseValue * 255)).getRGB());
            }

        revalidate();
        repaint();
    }

}
