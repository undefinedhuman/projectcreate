package de.undefinedhuman.sandboxgame.editor.editor.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import me.gentlexd.sandboxeditor.engine.file.*;
import me.gentlexd.sandboxeditor.engine.ressources.RessourceManager;
import me.gentlexd.sandboxeditor.engine.window.Window;

import javax.swing.*;
import java.awt.*;

public class BlockTextureSetting extends Setting {

    private TextField textField;
    private Button button;

    private JCheckBox checkBox;
    private JLabel label;

    private java.awt.Color color;

    public BlockTextureSetting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        textField = new TextField(String.valueOf(value));

        button = new Button();
        button.addActionListener(e -> {
            color = JColorChooser.showDialog(null,"Change Button Background", color);
            button.setBackground(color);
        });
        button.setBounds((int) position.x + 225, (int) position.y,25,25);

        checkBox = new JCheckBox();
        checkBox.setSelected(true);
        checkBox.setBounds((int) position.x + 275, (int) position.y,25,25);

        label = new JLabel("Rotated");
        label.setBounds((int) position.x + 325, (int) position.y,75,25);

        panel.add(textField);
        panel.add(button);
        panel.add(checkBox);
        panel.add(label);

    }

    @Override
    public Object getValue() {

        return textField.getText();

    }

    @Override
    public void update() {

        textField.setBounds((int) position.x + 110, (int) position.y, 100, 25);
        button.setBounds((int) position.x + 110 + 125, (int) position.y, 25, 25);
        checkBox.setBounds((int) position.x + 110 + 175, (int) position.y, 25, 25);
        label.setBounds((int) position.x + 110 + 225, (int) position.y, 75, 25);

    }

    @Override
    public void save(FileWriter writer) {

        Gdx.app.postRunnable(() -> saveBlock(Integer.valueOf(Window.instance.editor.settings.get(0).getValue().toString())));
        writer.writeString(textField.getText());

    }

    @Override
    public void load(LineSplitter splitter, int id) {

        textField.setText(splitter.getNextString());

        FsFile file = new FsFile(Paths.RES_FOLDER,"itemColor/" + id + "/color.txt",false);
        if(!file.isEmpty()) {

            FileReader reader = file.getFileReader(true);
            reader.nextLine();
            Vector3 vecColor = reader.getNextVector3();
            color = new java.awt.Color((int) vecColor.x, (int) vecColor.y, (int) vecColor.z);
            button.setBackground(color);
            checkBox.setSelected(reader.getNextBoolean());
            reader.close();

        }

    }

    @Override
    protected void addGuiSetting() {

        panel.add(textField);
        panel.add(button);
        panel.add(checkBox);
        panel.add(label);

    }

    private void saveBlock(int id) {

        createBlockTexture(id, textField.getText(), checkBox.isSelected());

        TexturePacker.Settings setting = new TexturePacker.Settings();
        setting.maxWidth = 2048; setting.maxHeight = 2048; setting.paddingX = 2; setting.paddingY = 2; setting.alphaThreshold = 0; setting.filterMin = Texture.TextureFilter.Nearest; setting.filterMag = Texture.TextureFilter.Nearest;
        setting.fast = false; setting.edgePadding = true; setting.stripWhitespaceX = true; setting.stripWhitespaceY = true; setting.rotation = false; setting.bleed = true; setting.grid = false;
        setting.duplicatePadding = true; setting.pot = false; setting.alias = true; setting.ignoreBlankImages = false; setting.debug = false; setting.useIndexes = false; setting.premultiplyAlpha = false; setting.limitMemory = false;
        TexturePacker.process(setting,textField.getText() + "-temp/", Paths.ITEM_PATH.getPath() + id + "/",textField.getText() + ".atlas");

        FsFile fileDir = new FsFile(textField.getText() + "-temp/", true);
        FileManager.deleteFile(fileDir.getFile());

    }

    private void createBlockTexture(int id, String name, boolean rotate) {

        if(rotate) {

            for(int i = 0; i < 6; i++) {

                if(i == 2 || i == 4 || i == 5) for(int j = 0; j < 4; j++) saveRotatedBlockTexture(i, j, name);
                else if(i == 3) { saveRotatedBlockTexture(i,0, name); saveRotatedBlockTexture(i,1, name);
                } else saveRotatedBlockTexture(i,0, name);

            }

        } else {

            for(int i = 0; i < 6; i++) {

                Pixmap pixmap = createBlockTexture(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1),0);
                FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_0.png");
                if(!handle.exists()) PixmapIO.writePNG(handle, pixmap);

            }

            for(int i = 0; i < 6; i++) {

                if(i == 2 || i == 4 || i == 5) { for(int j = 0; j < 4; j++) if(j == 0) saveRotateBlockTexture(i,0, i, 0, name,false); else saveRotateBlockTexture(i,j-1, i, j, name, true);
                } else if(i == 3) { saveRotateBlockTexture(i,0, i,0, name, false); saveRotateBlockTexture(i,0, i,1, name,true);
                } else saveRotateBlockTexture(i,0, i,0, name, false);

            }

        }

        Pixmap pixmap = createBlockTexture(6, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1),0);
        FileHandle handle1 = Gdx.files.local(Paths.ITEM_PATH.getPath() + id + "/" + name + " Icon.png");
        PixmapIO.writePNG(handle1, pixmap);

        FsFile file = new FsFile(Paths.RES_FOLDER,"itemColor/" + id + "/color.txt", false);
        FileWriter writer = file.getFileWriter(true);
        writer.writeVector3(new Vector3(color.getRed(), color.getGreen(), color.getBlue()));
        writer.writeBoolean(checkBox.isSelected());
        writer.close();

    }

    private void saveRotateBlockTexture(int i, int j, int i1, int j1, String name, boolean rotate) {

        Texture texture = RessourceManager.loadTexture(name + "-temp/" + name + "_" + i + "_" + j + ".png");
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        if(rotate) pixmap = rotatePixmap(pixmap);
        FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i1 + "_" + j1 + ".png");
        PixmapIO.writePNG(handle, pixmap);

    }

    private void saveRotatedBlockTexture(int i, int j, String name) {

        Pixmap pixmap = createBlockTexture(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1), j);
        Texture texture = new Texture(pixmap);
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        Pixmap pixmap1 = texture.getTextureData().consumePixmap();
        FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_" + j + ".png");
        PixmapIO.writePNG(handle, pixmap1);

    }

    private Pixmap createBlockTexture(int id, String textureName, Color outlineColor, int rotate) {

        Pixmap templatePix = getInternalPixmap("template/Sprite-Template-" + id + ".png"), texturePix = getInternalPixmap(textureName + ".png"), spritePix = new Pixmap(templatePix.getWidth(), templatePix.getHeight(), Pixmap.Format.RGBA8888);
        for(int i = 0; i < rotate; i++) templatePix = rotatePixmap(templatePix);
        for(int i = 0; i < templatePix.getWidth(); i++) for(int j = 0; j < templatePix.getHeight(); j++) if(templatePix.getPixel(i, j) == Color.rgba8888(0,0,1,1) || templatePix.getPixel(i, j) == Color.rgba8888(0,1,0,1)) spritePix.drawPixel(i, j, templatePix.getPixel(i, j) == Color.rgba8888(0,0,1,1) ? texturePix.getPixel(i, j) : Color.rgba8888(outlineColor));
        return spritePix;

    }

    private Pixmap getInternalPixmap(String path) {

        Texture baseTexture = new Texture(Gdx.files.internal(path));
        if (!baseTexture.getTextureData().isPrepared()) baseTexture.getTextureData().prepare();
        return baseTexture.getTextureData().consumePixmap();

    }

    private Pixmap rotatePixmap(Pixmap srcPix) {

        int width = srcPix.getWidth(), height = srcPix.getHeight();
        Pixmap rotatedPix = new Pixmap(width, height, srcPix.getFormat());
        for(int y = 0; y < width; y++) for(int x = 0; x < height; x++) rotatedPix.drawPixel(height-y-1, x, srcPix.getPixel(x, y));
        return rotatedPix;

    }

}
