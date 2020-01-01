package de.undefinedhuman.sandboxgame.world;

import de.undefinedhuman.sandboxgame.utils.Tools;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class NoiseTest extends JFrame {

    private Random random = new Random();

    private int seed, octaves;
    private float amplitude, roughness;

    public NoiseTest(int octaves, float amplitude, float roughness, int seed) {

        this.octaves = octaves;
        this.amplitude = amplitude;
        this.roughness = roughness;
        this.seed = seed;

    }

    private JLabel drawingLabel;

    int oct = 6;
    float ampl = 0.75f, rough = 0.4f, tresh = 0.25f;

    public NoiseTest() {

        setTitle("Noise Test");
        setSize(1200,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        drawingLabel = new JLabel();
        drawingLabel.setSize(800,800);
        add(drawingLabel);
        setResizable(false);

        JLabel label2 = new JLabel();
        label2.setBounds(800,0,200,800);

        JTextField noiseThreshhold = new JTextField(String.valueOf(tresh));
        noiseThreshhold.setBounds(810,25, 100,25);
        label2.add(noiseThreshhold);

        JTextField octB = new JTextField(String.valueOf(oct));
        octB.setBounds(810,60, 100,25);
        label2.add(octB);

        JTextField ampB = new JTextField(String.valueOf(ampl));
        ampB.setBounds(810,95, 100,25);
        label2.add(ampB);

        JTextField roughB = new JTextField(String.valueOf(rough));
        roughB.setBounds(810,130, 100,25);
        label2.add(roughB);

        JTextField seedB = new JTextField("3455467");
        seedB.setBounds(810,165, 100,25);
        label2.add(seedB);

        JTextField borderB = new JTextField("25");
        borderB.setBounds(810,200, 100,25);
        label2.add(borderB);

        JButton noiseTresh = new JButton();
        noiseTresh.setBounds(810,235,100, 25);
        noiseTresh.addActionListener(e -> setNoise(Integer.parseInt(octB.getText()), Float.parseFloat(ampB.getText()), Float.parseFloat(roughB.getText()), Float.parseFloat(noiseThreshhold.getText()), Float.parseFloat(borderB.getText()), Integer.parseInt(seedB.getText())));
        label2.add(noiseTresh);

        add(label2);

        setNoise(oct, ampl, rough, tresh, 25, new Random().nextInt(1000000));
        setVisible(true);

    }

    private void setNoise(int octaves, float amplitude, float roughness, float threshG, float border, int seed) {

        int width = 800, height = 800;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Noise noise = new Noise(octaves,amplitude,roughness);
        noise.setSeed(seed);

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                float k = noise.select(threshG, (x < border ? (x%border)/border : x > width - border ? 1f-(x%border)/border : 1f) *  noise.fractal(x, y)) ? 1 : 0;

                int a = 255, r = (int) (k * 255f), g = (int) (k * 255f), b = (int) (k * 255f);
                int p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);

            }

        }
        drawingLabel.setIcon(new ImageIcon(img));

    }

    public static void main(String[] args) {
        new NoiseTest();
    }

    private static float calcBorder(float x, float borderSize) {
        return Tools.lerp(0,1,(x % borderSize) / borderSize);
    }

    public float getInterpolatedNoise(float x, float y) {

        int intX = (int) x, intY = (int) y;
        float fracX = x - intX, fracY = y - intY;

        float v1 = getSmoothNoise(intX, intY), v2 = getSmoothNoise(intX + 1, intY), v3 = getSmoothNoise(intX, intY + 1), v4 = getSmoothNoise(intX + 1, intY + 1);
        float i1 = cosInterpolation(v1, v2, fracX), i2 = cosInterpolation(v3, v4, fracX);
        return cosInterpolation(i1, i2, fracY);

    }

    private float getSmoothNoise(int x, int y) {

        float corners = (getNoise(x - 1, y - 1) + getNoise(x + 1, y - 1) + getNoise(x - 1, y + 1) + getNoise(x + 1, y + 1)) / 16f;
        float sides = (getNoise(x - 1, y) + getNoise(x + 1, y) + getNoise(x, y - 1) + getNoise(x, y + 1)) / 8f;
        float center = getNoise(x, y) / 4f;
        return corners + sides + center;

    }

    private float getNoise(int x, int y) {

        random.setSeed(x * 49632 + y * 325176 + seed);
        return random.nextFloat() * 2f - 1f;

    }

    public float gradient(float x, float y, float maxY) {

        float value = (float) 1 / maxY * y;
        value += scale(0.5f, 0, fractal(x, y));
        return linInterpolation(0, 1, value);

    }

    public float select(float min, float max, float threshhold, float value) {
        return value >= threshhold ? max : min;
    }

    public boolean select(float threshhold, float value) {
        return value >= threshhold;
    }

    public float scale(float scale, float offset, float value) {

        return value * scale + offset;

    }

    private float cosInterpolation(float a, float b, float blend) {

        float c = (float) ((1f - Math.cos((blend * Math.PI))) * 0.5f);
        return a * (1 - c) + b * c;

    }

    public float linInterpolation(float a, float b, float v) {
        return (a * (b - v) + b * v);
    }

    public float fractal(float x, float y) {

        float value = 0;
        float d = (float) Math.pow(2, octaves - 1);

        for (int i = 0; i < octaves; i++) {

            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) Math.pow(roughness, i) * amplitude;
            value += getInterpolatedNoise(x * freq, y * freq) * amp;

        }

        return Math.abs(value);

    }

}