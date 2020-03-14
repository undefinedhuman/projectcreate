package de.undefinedhuman.sandboxgame.editor.editor.settings;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.EntityEditor;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.utils.Tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class PositionSetting extends Setting {

    private BufferedImage texture;
    private JLabel label;
    private JTextField text;

    private boolean offset;

    public PositionSetting(JPanel panel, String name, Object value, boolean offset) {
        this(panel, name, value, offset,true);
    }

    public PositionSetting(JPanel panel, String name, Object value, boolean offset, boolean add) {

        super(panel, name, value, add);
        this.offset = offset;
        text = new JTextField();

        try {
            texture = ImageIO.read(new File("./editor/assets/blank.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        ImageIcon imageIcon = new ImageIcon(texture);

        label = new JLabel();
        label.setIcon(imageIcon);
        label.setBounds(250,40,50,50);

        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {

                String[] name;

                JFileChooser chooser = new JFileChooser();

                chooser.setCurrentDirectory(new File("./editor/assets"));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG & GIF Images", "png", "gif");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {

                    name = chooser.getSelectedFile().getName().split("\\.");

                    String texturePath;

                    texturePath = chooser.getSelectedFile() != null ? "./editor/assets/" + name[0] + ".png" : "./editor/assets/Unknown.png";

                    try {
                        texture = ImageIO.read(new File(texturePath));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    text.setText(calculateVectors(texture, offset));
                    setValue(calculateVectors(texture, offset));

                    Icon imageIcon = new ImageIcon(texture);
                    label.setIcon(imageIcon);

                }

            }

        });

        if(add) panel.add(label);

    }

    @Override
    public void update() {

        label.setBounds((int) position.x + 110, (int) position.y, 25, 25);
        text.setBounds((int) position.x + 145, (int) position.y,100,25);

    }

    @Override
    public void save(FileWriter writer) {

        String[] values = text.getText().split(";");
        for(String s : values) writer.writeString(s);

    }

    @Override
    public void load(LineSplitter splitter, int id) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File("./editor/assets/blank.png"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.instance.crash();
        }

        assert image != null;
        Icon imageIcon = new ImageIcon(image);

        String s = splitter.getData();

        text.setText(s);
        setValue(s);
        label.setIcon(imageIcon);

    }

    @Override
    protected void addGuiSetting() {

        panel.add(label);
        panel.add(text);

    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(String texturePath) {

        File file;

        try {
            file = new File(texturePath);
            texture = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Icon imageIcon = new ImageIcon(texture);

        text.setText(calculateVectors(texture, offset));
        setValue(calculateVectors(texture, offset));
        label.setIcon(imageIcon);

    }

    private String calculateVectors(BufferedImage currentImage, boolean offset) {

        BufferedImage image = Tools.scaleNearest(currentImage,0.5f);

        int size = image.getWidth() / 60;
        Vector2[] vectors = new Vector2[size];

        for(int i = 0; i < size; i++) for(int x = 0; x < 60; x++) for(int y = 0; y < image.getHeight(); y++) {

            if(new Color(image.getRGB(i * 60 + x,image.getHeight() - 1 - y)).getRed() == 255) {
                vectors[i] = new Vector2(x * 2,y * 2);
                if(i != 0 && offset) vectors[i] = new Vector2(vectors[0].x - vectors[i].x, vectors[0].y - vectors[i].y);
                break;
            }

        }

        if(offset) vectors[0] = new Vector2(0,0);

        StringBuilder builder = new StringBuilder();
        builder.append(vectors.length).append(";");
        for(Vector2 vec : vectors) builder.append(vec.x).append(";").append(vec.y).append(";");
        return builder.toString();

    }

}
