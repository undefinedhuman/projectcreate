package de.undefinedhuman.projectcreate.editor.editor.world;

import de.undefinedhuman.projectcreate.core.noise.Noise;
import de.undefinedhuman.projectcreate.core.noise.generator.OpenSimplex2S;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Preview implements Runnable {

    private JLabel previewTextureLabel;
    private BufferedImage noiseImage;
    private long seed = 1337;
    private int octaves = 3;
    private float frequency = 0.01f;
    private float lacunarity = 2f;
    private float gain = 0.5f;
    private float amplitudeFactor = 0;

    public Preview(JLabel previewTextureLabel) {
        this.previewTextureLabel = previewTextureLabel;
        this.noiseImage = new BufferedImage(previewTextureLabel.getWidth(), previewTextureLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        previewTextureLabel.setIcon(new ImageIcon(noiseImage));
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public void setOctaves(float octaves) {
        this.octaves = (int) octaves;
    }

    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public void setAmplitudeFactor(float amplitudeFactor) {
        this.amplitudeFactor = amplitudeFactor;
    }

    @Override
    public void run() {
        OpenSimplex2S openSimplex = new OpenSimplex2S(seed);
        Noise noise = new Noise(openSimplex);
        noise.setFrequency(frequency);
        noise.setOctaves(octaves);
        noise.setLacunarity(lacunarity);
        noise.setGain(gain);
        noise.setAmplitudeFactor(amplitudeFactor);

        for(int x = 0; x < noiseImage.getWidth(); x++)
            for(int y = 0; y < noiseImage.getHeight(); y++) {
                double noiseValue = noise.select(0f, 1f, 0.5f, noise.calculateFBm(x, y) * 0.5f + 0.5f);
                noiseImage.setRGB(x, y, new Color((int) (noiseValue * 255), (int) (noiseValue * 255), (int) (noiseValue * 255)).getRGB());
            }

        previewTextureLabel.revalidate();
        previewTextureLabel.repaint();
    }

}
