package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.math.Vector2;
import com.sixlegs.png.PngImage;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
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

    private BufferedImage texture;
    private JLabel textureLabel;

    public TextureSetting(String key, Object value) {
        super(SettingType.Texture, key, value);
        loadTexture("Unknown.png");
        setValue("Unknown.png");
        this.offset = 200;
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        textureLabel = new JLabel();
        textureLabel.setHorizontalAlignment(JLabel.CENTER);
        textureLabel.setBounds((int) position.x, (int) position.y, 200, 200);
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
                if(textureFile == null) return;
                loadTexture(textureFile.getPath());
                setValue(textureFile.getName());
                setTextureIcon();
            }
        });
        panel.add(textureLabel);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeString(value.toString());
        FsFile file = new FsFile(writer.getParentDirectory().path() + Variables.FILE_SEPARATOR + getString(),  Files.FileType.Local);
        try { ImageIO.write(texture, "png", file.file());
        } catch (IOException e) { Log.instance.exit(e.getMessage()); }
    }

    @Override
    public void load(FsFile parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        setValue(((LineSplitter) value).getNextString());
        String path = parentDir.path() + Variables.FILE_SEPARATOR + getString();
        try {
            loadTexture(path);
            if(texture == null)
                loadTexture("Unknown.png");
        } catch (Exception e) { Log.instance.exit(e.getMessage()); }
        if(TextureManager.instance != null) {
            setValue(path);
            TextureManager.instance.addTexture(getString());
        }
    }

    @Override
    protected void setValueInMenu(Object value) {
        setTextureIcon();
    }

    @Override
    protected void delete() {
        if(TextureManager.instance != null) TextureManager.instance.removeTexture(getString());
    }

    private void loadTexture(String path) {
        try { texture = new PngImage().read(new FsFile(path, Files.FileType.Internal).read(), true);
        } catch (IOException ex) { Log.instance.exit(ex.getMessage()); }
        if(texture == null && !path.equals("Unknown.png")) loadTexture("Unknown.png");
    }

    private void setTextureIcon() {
        if(textureLabel == null) return;
        float scaleFactor = (float) textureLabel.getHeight() / texture.getHeight();
        textureLabel.setIcon(new ImageIcon(texture.getScaledInstance((int) (texture.getWidth() * scaleFactor), (int) (texture.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
    }

}
