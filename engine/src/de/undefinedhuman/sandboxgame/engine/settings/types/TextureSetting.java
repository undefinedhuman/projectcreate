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
            public void mouseClicked(MouseEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new FsFile("editor/", false).getFile());
                chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal != JFileChooser.APPROVE_OPTION) return;
                File textureFile = chooser.getSelectedFile();
                if(textureFile == null) return;

                try { texture = ImageIO.read(textureFile);
                } catch (Exception e) { Log.instance.crash(e.getMessage()); }
                textureLabel.setIcon(new ImageIcon(texture));
                setValue(textureFile.getName());
            }

        });
        panel.add(textureLabel);
    }

    @Override
    public void save(FsFile parentDir, FileWriter writer) {
        writer.writeString(getString());
        FsFile file = new FsFile(parentDir, getString(), false);
        try { ImageIO.write(texture, "png", file.getFile());
        } catch (IOException e) { Log.instance.crash(e.getMessage()); }
    }

    @Override
    public void load(FsFile parentDir, LineSplitter splitter) {
        value = splitter.getNextString();
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

}
