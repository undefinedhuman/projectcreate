package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.sixlegs.png.PngImage;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
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

public class TextureSetting extends Setting<String> {

    private static final int PREVIEW_TEXTURE_LABEL_SIZE = 128;

    private BufferedImage texture;
    private JLabel textureLabel;

    public TextureSetting(String key, String defaultValue) {
        super(key, defaultValue);
        loadTexture(defaultValue);
        setContentHeight(PREVIEW_TEXTURE_LABEL_SIZE);
    }

    @Override
    protected void saveValue(FileWriter writer) {
        writer.writeString(getValue());
        FsFile file = new FsFile(writer.parent(), getValue(),  Files.FileType.Local);
        try { ImageIO.write(texture, "png", file.file());
        } catch (IOException ex) { Log.showErrorDialog("Can not save texture (" + this + "): \n" + ex.getMessage(), true); }
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        textureLabel = new JLabel();
        textureLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, getContentHeight()));
        textureLabel.setPreferredSize(new Dimension(Integer.MIN_VALUE, getContentHeight()));
        textureLabel.setMinimumSize(new Dimension(Integer.MIN_VALUE, getContentHeight()));
        textureLabel.setLayout(new BorderLayout());
        textureLabel.setHorizontalAlignment(JLabel.CENTER);
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
        accordion.addCollapsiblePanel(key, textureLabel);
    }

    @Override
    public void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        setValue(((LineSplitter) value).getNextString());
        String path = parentDir.path() + Variables.FILE_SEPARATOR + getValue();
        loadTexture(path);
        if(texture == null)
            loadTexture(path = "Unknown.png");
        if(Variables.IS_EDITOR)
            return;
        setValue(path);
        TextureManager.getInstance().loadTextures(getValue());
    }

    @Override
    protected void updateMenu(String value) {
        setTextureIcon();
    }

    @Override
    protected void delete() {
        if(TextureManager.getInstance() == null)
            return;
        TextureManager.getInstance().removeTextures(getValue());
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
        } catch (IOException ex) { Log.showErrorDialog("Can not load texture (" + this + "): \n" + ex.getMessage(), true); }
        if(texture == null && !path.equals("Unknown.png"))
            loadTexture("Unknown.png");
        setValue(textureFile.name());
    }

    private void setTextureIcon() {
        if(textureLabel == null)
            return;
        float scaleFactor = (float) PREVIEW_TEXTURE_LABEL_SIZE / texture.getHeight();
        textureLabel.setIcon(new ImageIcon(texture.getScaledInstance((int) (texture.getWidth() * scaleFactor), (int) (texture.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
    }

}
