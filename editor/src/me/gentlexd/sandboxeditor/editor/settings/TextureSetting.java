package me.gentlexd.sandboxeditor.editor.settings;

import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.engine.window.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureSetting extends Setting {

    private BufferedImage texture;
    private String texturePath1 = "Unknown.png";
    private JLabel label;

    public TextureSetting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        try {
            texture = ImageIO.read(new File("./core/assets/Unknown.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        ImageIcon imageIcon = new ImageIcon(texture);

        label = new JLabel();
        label.setIcon(imageIcon);
        label.setSize(50, 50);
        label.setLocation(250, 40);

        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {

                String[] name;

                JFileChooser chooser = new JFileChooser();

                chooser.setCurrentDirectory(new File("./core/assets"));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG & GIF Images", "png", "gif");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {

                    name = chooser.getSelectedFile().getName().split("\\.");

                    String texturePath;

                    texturePath = chooser.getSelectedFile() != null ? "./core/assets/" + name[0] + ".png" : "./core/assets/Unknown.png";
                    setValue(chooser.getSelectedFile() != null ? name[0] + ".png" : "Unknown.png");

                    texturePath1 = texturePath;

                    try {
                        texture = ImageIO.read(new File(texturePath));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    Icon imageIcon = new ImageIcon(texture);
                    label.setIcon(imageIcon);

                }

            }

        });

        panel.add(label);

    }

    public TextureSetting(JPanel panel, String name, Object value, boolean add) {

        super(panel, name, value, add);

        try {
            texture = ImageIO.read(new File("./core/assets/Unknown.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        ImageIcon imageIcon = new ImageIcon(texture);

        label = new JLabel();
        label.setIcon(imageIcon);
        label.setSize(50, 50);
        label.setLocation(250, 40);

        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {

                String[] name;

                JFileChooser chooser = new JFileChooser();

                chooser.setCurrentDirectory(new File("./core/assets"));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG & GIF Images", "png", "gif");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {

                    name = chooser.getSelectedFile().getName().split("\\.");

                    String texturePath;

                    texturePath = chooser.getSelectedFile() != null ? "./core/assets/" + name[0] + ".png" : "./core/assets/Unknown.png";
                    setValue(chooser.getSelectedFile() != null ? name[0] + ".png" : "Unknown.png");

                    texturePath1 = texturePath;

                    try {
                        texture = ImageIO.read(new File(texturePath));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

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

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(String.valueOf(getValue()));

        try {

            File file = new File(Window.instance.editor.getPath().getPath() + Window.instance.editor.settings.get(0).getValue() + "/" + getValue());
            if(file.createNewFile()) Log.info("Texture: " + file.getName() + " wurde erfolgreich erstellt!");
            ImageIO.write(texture, "png", file);

        } catch (IOException e) {

            System.out.println(e.getMessage());
            System.exit(3);

        }

    }

    @Override
    public void load(FileReader reader, int id) {

        String textureName = reader.getNextString();

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(Window.instance.editor.getPath().getPath() + id + "/" + textureName));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Icon imageIcon = new ImageIcon(image);

        texturePath1 = Window.instance.editor.getPath().getPath() + id + "/" + textureName;

        setValue(textureName);
        label.setIcon(imageIcon);

    }

    @Override
    protected void addGuiSetting() {

        panel.add(label);

    }

    public BufferedImage getTexture() {
        return texture;
    }

    public String getTexturePath1() {
        return texturePath1;
    }

    public void setTexture(String texturePath) {

        File file = null;

        try {
            file = new File(texturePath);
            texture = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Icon imageIcon = new ImageIcon(texture);

        texturePath1 = texturePath;

        setValue(file.getName().split("\\.")[0] + ".png");
        label.setIcon(imageIcon);

    }

}
