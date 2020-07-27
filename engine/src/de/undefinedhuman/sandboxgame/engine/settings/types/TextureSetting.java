package de.undefinedhuman.sandboxgame.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

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
    private JLabel textureLabel;

    public TextureSetting(String key, Object value) {
        super(SettingType.Texture, key, value);
        try { texture = ImageIO.read(new FsFile("Unknown.png", false).getFile());
        } catch (IOException e) { e.printStackTrace(); }
        setValue("Unknown.png");
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        textureLabel = new JLabel(new ImageIcon(texture));
        textureLabel.setBounds((int) position.x, (int) position.y, 25, 25);
        textureLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                mouseClickEvent(event);
            }
        });
        panel.add(textureLabel);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeString(value.toString());
        FsFile file = new FsFile(writer.getParentDirectory().getPath() + Variables.FILE_SEPARATOR + getString(), false);
        try { ImageIO.write(texture, "png", file.getFile());
        } catch (IOException e) { Log.instance.crash(e.getMessage()); }
    }

    @Override
    public void load(FsFile parentDir, Object value) {
        if(!(value instanceof LineSplitter)) return;
        setValue(((LineSplitter) value).getNextString());
        try {
            loadTexture(parentDir.getPath() + Variables.FILE_SEPARATOR + getString());
            if(texture == null) loadTexture("Unknown.png");
        } catch (Exception e) { Log.instance.crash(e.getMessage()); }
        if(TextureManager.instance != null) {
            setValue(parentDir.getPath() + Variables.FILE_SEPARATOR + getString());
            TextureManager.instance.addTexture(getString());
        }
    }

    private void loadTexture(String path) throws IOException {
        texture = ImageIO.read(new FsFile(path, false).getFile());
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(textureLabel != null) textureLabel.setIcon(new ImageIcon(texture));
    }

    @Override
    protected void delete() {
        if(TextureManager.instance != null) TextureManager.instance.removeTexture(getString());
    }

    public void mouseClickEvent(MouseEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new FsFile("editor/", false).getFile());
        chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal != JFileChooser.APPROVE_OPTION) return;
        File textureFile = chooser.getSelectedFile();
        if(textureFile == null) return;
        loadTexture(textureFile);
    }

    public void loadTexture(File textureFile) {
        try { texture = ImageIO.read(textureFile);
        } catch (Exception e) { Log.instance.crash(e.getMessage()); }
        textureLabel.setIcon(new ImageIcon(texture));
        setValue(textureFile.getName());
    }

}
