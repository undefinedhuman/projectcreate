package de.undefinedhuman.projectcreate.world;

import de.undefinedhuman.projectcreate.utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NoiseTest extends JFrame {

    int oct = 3;
    float ampl = 1f, rough = 0.1f, tresh = 0.1f;
    private JLabel drawingLabel;

    private BufferedImage img;

    public NoiseTest() {

        setTitle("Noise Test");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        drawingLabel = new JLabel();
        drawingLabel.setSize(800, 800);
        add(drawingLabel);
        setResizable(false);

        JLabel label2 = new JLabel();
        label2.setBounds(800, 0, 200, 800);

        JLabel thresholdLabel = new JLabel("Threshold");
        thresholdLabel.setBounds(920, 25, 100, 25);
        JTextField threshold = new JTextField(String.valueOf(tresh));
        threshold.setBounds(810, 25, 100, 25);
        label2.add(threshold);
        label2.add(thresholdLabel);

        JLabel octavesLabel = new JLabel("Octaves");
        octavesLabel.setBounds(920, 60, 100, 25);
        label2.add(octavesLabel);
        JTextField octaves = new JTextField(String.valueOf(oct));
        octaves.setBounds(810, 60, 100, 25);
        label2.add(octaves);

        JLabel amplitudeLabel = new JLabel("Amplitude");
        amplitudeLabel.setBounds(920, 95, 100, 25);
        label2.add(amplitudeLabel);
        JTextField amplitude = new JTextField(String.valueOf(ampl));
        amplitude.setBounds(810, 95, 100, 25);
        label2.add(amplitude);

        JLabel roughnessLabel = new JLabel("Roughness");
        roughnessLabel.setBounds(920, 130, 100, 25);
        label2.add(roughnessLabel);
        JTextField roughness = new JTextField(String.valueOf(rough));
        roughness.setBounds(810, 130, 100, 25);
        label2.add(roughness);

        JLabel seedLabel = new JLabel("Seed");
        seedLabel.setBounds(920, 165, 100, 25);
        label2.add(seedLabel);
        JTextField seed = new JTextField("3455467");
        seed.setBounds(810, 165, 100, 25);
        label2.add(seed);

        JButton generate = new JButton("Generate");
        generate.setBounds(810, 235, 100, 25);
        generate.addActionListener(e -> setNoise(
                Integer.parseInt(octaves.getText()),
                Float.parseFloat(amplitude.getText()),
                Float.parseFloat(roughness.getText()),
                Float.parseFloat(threshold.getText()),
                Integer.parseInt(seed.getText())));
        label2.add(generate);

        JButton random = new JButton("Random");
        random.setBounds(810, 305, 100, 25);
        random.addActionListener(e -> setNoise(
                Utils.clamp(new Random().nextInt(12), 5, 10),
                Utils.clamp(new Random().nextFloat() * 2f, 0.1f, 1.8f),
                Utils.clamp(new Random().nextFloat() * 1.5f, 0.2f, 1.5f),
                Utils.clamp(new Random().nextFloat(), 0.25f, 0.75f),
                new Random().nextInt(1000000000)));
        label2.add(random);

        JButton saveFile = new JButton("Save");
        saveFile.setBounds(810, 270, 100, 25);
        saveFile.addActionListener(e -> saveBackground());
        label2.add(saveFile);

        add(label2);

        setNoise(oct, ampl, rough, tresh, new Random().nextInt(1000000));
        setVisible(true);

    }

    private void setNoise(int octaves, float amplitude, float roughness, float threshold, int seed) {
        int width = 640, height = 640;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Noise noise = new Noise(octaves, amplitude, roughness);
        noise.setSeed(seed);

        for (int i = 0; i < height/2; i++) {

            for (int j = 0; j < width/2; j++) {

                int y = i*2, x = j*2;
                float k = noise.select(threshold, (1f - noise.calculateFractalNoise(x, y))) ? 1 : 0;
                int a = 255, r = (int) (k * 255f), g = (int) (k * 255f), b = (int) (k * 255f);
                int p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
                img.setRGB(x, y+1, p);
                img.setRGB(x+1, y, p);
                img.setRGB(x+1, y+1, p);
            }

        }
        drawingLabel.setIcon(new ImageIcon(img));

    }

    public static void main(String[] args) {
        new NoiseTest();
    }

    private void saveBackground() {
        try {
            File file = new File("test.png");
            ImageIO.write(img, "png", file);
            System.out.println("Hallo");
        } catch(IOException e) {
            System.out.println("Error: " + e);
        }
    }

}