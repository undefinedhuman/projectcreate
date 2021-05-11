package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.sixlegs.png.PngImage;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureSetting extends Setting {

    private static final int PREVIEW_TEXTURE_LABEL_SIZE = 128;

    private BufferedImage texture;
    private JLabel textureLabel;

    public TextureSetting(String key, Object value) {
        super(SettingType.Texture, key, value);
        loadTexture(Variables.DEFAULT_TEXTURE);
        setContentHeight(PREVIEW_TEXTURE_LABEL_SIZE);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        textureLabel = new JLabel();
        textureLabel.setHorizontalAlignment(JLabel.CENTER);
        textureLabel.setBounds(0, 0, width, PREVIEW_TEXTURE_LABEL_SIZE);
        setTextureIcon();
        textureLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new FsFile(Paths.EDITOR_PATH, Files.FileType.Internal).file());
                chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal != JFileChooser.APPROVE_OPTION) return;
                File textureFile = chooser.getSelectedFile();
                if(textureFile == null)
                    return;
                setTexture(textureFile.getPath(), Files.FileType.Internal);
            }
        });
        panel.add(textureLabel);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeString(value.toString());
        FsFile file = new FsFile(writer.parent(), getString(),  Files.FileType.Local);
        try { ImageIO.write(texture, "png", file.file());
        } catch (IOException ex) { Log.showErrorDialog(Level.CRASH, "Can not save texture (" + this + "): \n" + ex.getMessage(), true); }
    }

    @Override
    public void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        setValue(((LineSplitter) value).getNextString());
        String path = parentDir.path() + Variables.FILE_SEPARATOR + getString();
        loadTexture(path);
        if(texture == null)
            loadTexture(path = "Unknown.png");
        if(TextureManager.instance == null)
            return;
        setValue(path);
        TextureManager.instance.loadTextures(getString());
    }

    @Override
    protected void setValueInMenu(Object value) {
        setTextureIcon();
    }

    @Override
    protected void delete() {
        if(TextureManager.instance == null)
            return;
        TextureManager.instance.removeTextures(getString());
    }

    public void setTexture(String path, Files.FileType type) {
        loadTexture(path, type);
        setTextureIcon();
    }

    private void loadTexture(String path) {
        loadTexture(path, Files.FileType.Internal);
    }

    private void loadTexture(String path, Files.FileType type) {
        FsFile textureFile = new FsFile(path, type);
        try { texture = new PngImage().read(textureFile.read(), true);
        } catch (IOException ex) { Log.showErrorDialog(Level.CRASH, "Can not load texture (" + this + "): \n" + ex.getMessage(), true); }
        if(texture == null && !path.equals("Unknown.png"))
            loadTexture("Unknown.png");
        setValue(textureFile.name());
    }

    private void setTextureIcon() {
        if(textureLabel == null)
            return;
        float scaleFactor = (float) textureLabel.getHeight() / texture.getHeight();
        textureLabel.setIcon(new ImageIcon(texture.getScaledInstance((int) (texture.getWidth() * scaleFactor), (int) (texture.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
    }

}
