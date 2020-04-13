package de.undefinedhuman.sandboxgame.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureOffsetSetting extends Setting {

    private BufferedImage texture;
    private JLabel textureLabel;
    private JTextField valueField;
    private boolean offset;

    public TextureOffsetSetting(String key, Object value, boolean offset) {
        super(SettingType.Texture, key, value);
        try { texture = ImageIO.read(new FsFile("Unknown.png", false).getFile());
        } catch (IOException e) { e.printStackTrace(); }
        this.offset = offset;
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        textureLabel = new JLabel(new ImageIcon(texture));
        textureLabel.setBounds((int) position.x, (int) position.y, 25, 25);
        textureLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new FsFile("Unknown.png", false).getFile());
                chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal != JFileChooser.APPROVE_OPTION) return;
                File textureFile = chooser.getSelectedFile();
                if(textureFile == null) return;

                BufferedImage texture;
                try { texture = ImageIO.read(textureFile);
                } catch (IOException e) { return; }
                setValue(calculateVectors(texture));
                setValueInMenu(getVector2Array());
            }

        });
        panel.add(textureLabel);
        valueField = createTextField(Tools.convertArrayToString(getVector2Array()), new Vector2(position).add(30, 0), new Vector2(170, 25), null);
        panel.add(valueField);
    }

    @Override
    public void save(FsFile parentDir, FileWriter writer) {
        Vector2[] vectors = getVector2Array();
        writer.writeInt(vectors.length);
        for(Vector2 vector : vectors) writer.writeVector2(vector);
    }

    @Override
    public void load(FsFile parentDir, LineSplitter splitter) {
        Vector2[] vectors = new Vector2[splitter.getNextInt()];
        for(int i = 0; i < vectors.length; i++) vectors[i] = splitter.getNextVector2();
        setValue(vectors);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(valueField != null) valueField.setText(Tools.convertArrayToString(getVector2Array()));
    }

    private Vector2[] calculateVectors(BufferedImage currentImage) {
        BufferedImage image = Tools.scaleNearest(currentImage, 0.5f);
        int size = image.getWidth() / Variables.PLAYER_TEXTURE_OFFSET_WIDTH;
        Vector2[] vectors = new Vector2[size];

        for(int i = 0; i < size; i++) for(int x = 0; x < Variables.PLAYER_TEXTURE_OFFSET_WIDTH; x++) for(int y = 0; y < image.getHeight(); y++) {
            if(new Color(image.getRGB(i * Variables.PLAYER_TEXTURE_OFFSET_WIDTH + x, image.getHeight() - 1 - y)).getRed() != 255) continue;
            vectors[i] = new Vector2(x * 2, y * 2);
            if(i != 0 && offset) vectors[i] = new Vector2(vectors[0].x - vectors[i].x, vectors[0].y - vectors[i].y);
            break;
        }

        if(offset) vectors[0] = new Vector2(0,0);
        return vectors;
    }

}
