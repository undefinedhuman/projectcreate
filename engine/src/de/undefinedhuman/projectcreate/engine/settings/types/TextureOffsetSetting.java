package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.panels.AsepriteUtils;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class TextureOffsetSetting extends Vector2ArraySetting {

    private BufferedImage texture;
    private JLabel textureLabel;
    private boolean offset;

    public TextureOffsetSetting(String key, Vector2[] value, boolean offset) {
        super(key, value);
        try { texture = ImageIO.read(new FsFile("Unknown.png", Files.FileType.Internal).read());
        } catch (IOException e) { e.printStackTrace(); }
        this.offset = offset;
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Variables.DEFAULT_CONTENT_HEIGHT));
        panel.setPreferredSize(new Dimension(Integer.MIN_VALUE, Variables.DEFAULT_CONTENT_HEIGHT));
        panel.setMinimumSize(new Dimension(Integer.MIN_VALUE, Variables.DEFAULT_CONTENT_HEIGHT));
        textureLabel = new JLabel(new ImageIcon(texture));
        textureLabel.setPreferredSize(new Dimension(Variables.DEFAULT_CONTENT_HEIGHT, Variables.DEFAULT_CONTENT_HEIGHT));
        textureLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new FsFile("editor/", Files.FileType.Internal).file());
                chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

                AsepriteUtils.loadAsepriteFile(dataFile -> {
                    JsonValue base = AsepriteUtils.JSON_READER.parse(dataFile);
                    JsonValue metaData = base.get("meta");

                    if(metaData == null)
                        return;

                    ArrayList<String> layers = new ArrayList<>();
                    metaData.get("layers").forEach(jsonValue -> {
                        if(!jsonValue.has("name"))
                            return;
                        layers.add(jsonValue.getString("name"));
                    });

                    JComboBox<String> layerSelection = new JComboBox<>(layers.toArray(new String[0]));

                    JPanel panel = new JPanel();
                    panel.add(layerSelection);

                    final int option = JOptionPane.showConfirmDialog(null, panel, "Select layer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(option == JOptionPane.CANCEL_OPTION)
                        return;
                    FsFile textureFile = new FsFile(dataFile.parent().path() + "/layers/" + layerSelection.getSelectedItem() + ".png", Files.FileType.Absolute);
                    if(FileError.checkFileForErrors("loading layer texture", textureFile, FileError.NULL, FileError.NON_EXISTENT, FileError.NO_FILE)) return;

                    BufferedImage texture;
                    try { texture = ImageIO.read(textureFile.file());
                    } catch (IOException e) { return; }
                    setValue(calculateVectors(texture));
                    updateMenu(getValue());
                });
            }

        });
        panel.add(textureLabel);
        valueField = Utils.createTextField(getKey(), serializer.serialize(getValue()), s -> setValue(parser.parse(s)), false, DEFAULT_VALUE);
        valueField.setPreferredSize(new Dimension(0, Variables.DEFAULT_CONTENT_HEIGHT));
        valueField.setEditable(false);
        panel.add(valueField, 0.9f);
        accordion.addContentPanel(key, panel);
    }

    private Vector2[] calculateVectors(BufferedImage currentImage) {
        float heightDivider = (float) Variables.PLAYER_TEXTURE_BASE_HEIGHT / (float) currentImage.getHeight();
        BufferedImage scaledImage = Utils.scaleNearest(currentImage, heightDivider);
        int size = scaledImage.getWidth() / Variables.PLAYER_TEXTURE_BASE_WIDTH;
        Vector2[] vectors = new Vector2[size];

        for(int i = 0; i < size; i++) for(int x = 0; x < Variables.PLAYER_TEXTURE_BASE_WIDTH; x++) for(int y = 0; y < scaledImage.getHeight(); y++) {
            if(new Color(scaledImage.getRGB(i * Variables.PLAYER_TEXTURE_BASE_WIDTH + x, scaledImage.getHeight() - 1 - y)).getRed() != 255) continue;
            vectors[i] = new Vector2(x * 2, y * 2);
            if(i != 0 && offset) vectors[i] = new Vector2(vectors[0].x - vectors[i].x, vectors[0].y - vectors[i].y);
            break;
        }

        if(offset) vectors[0] = new Vector2(0,0);
        return vectors;
    }

}
