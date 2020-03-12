package me.gentlexd.sandboxeditor.editor.settings;

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
import java.util.ArrayList;

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
            color = JColorChooser.showDialog(null, "Change Button Background", color);
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

        textField.setBounds((int) position.x + 110, (int) position.y,100,25);
        button.setBounds((int) position.x + 110 + 125, (int) position.y,25,25);
        checkBox.setBounds((int) position.x + 110 + 175, (int) position.y,25,25);
        label.setBounds((int) position.x + 110 + 225, (int) position.y,75,25);

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeString(textField.getText());
        saveBlock(Window.instance.editor.settings);

    }

    @Override
    public void load(FileReader reader, int id) {

        textField.setText(reader.getNextString());

        FileHandle handle2 = Gdx.files.local(Paths.RES_FOLDER.getPath() + "/itemColor/" + id + "/color.txt");
        if(handle2.exists()) {

            FileReader reader1 = new FileReader(handle2,true);
            reader1.nextLine();
            Vector3 vecColor = reader1.getNextVector3();
            color = new java.awt.Color((int) vecColor.x, (int) vecColor.y, (int) vecColor.z);
            button.setBackground(color);
            reader1.nextLine();
            checkBox.setSelected(reader1.getNextBoolean());
            reader1.close();

        }

    }

    @Override
    protected void addGuiSetting() {

        panel.add(textField);
        panel.add(button);
        panel.add(checkBox);
        panel.add(label);

    }

    private void saveBlock(ArrayList<Setting> settings) {

        if(checkBox.isSelected()) createBlockTexture(Integer.valueOf(settings.get(0).getValue().toString()), textField.getText());
        else createBlockTextureRotated(Integer.valueOf(settings.get(0).getValue().toString()), textField.getText());

        TexturePacker.Settings setting = new TexturePacker.Settings();
        setting.maxWidth = 2048;
        setting.maxHeight = 2048;
        setting.paddingX = 2;
        setting.paddingY = 2;
        setting.alphaThreshold = 0;
        setting.filterMin = Texture.TextureFilter.Nearest;
        setting.filterMag = Texture.TextureFilter.Nearest;
        setting.fast = false;
        setting.edgePadding = true;
        setting.stripWhitespaceX = true;
        setting.stripWhitespaceY = true;
        setting.rotation = false;
        setting.bleed = true;
        setting.grid = false;
        setting.duplicatePadding = true;
        setting.pot = false;
        setting.alias = true;
        setting.ignoreBlankImages = false;
        setting.debug = false;
        setting.useIndexes = false;
        setting.premultiplyAlpha = false;
        setting.limitMemory = false;
        TexturePacker.process(setting,textField.getText() + "-temp/", Paths.ITEM_PATH.getPath() + settings.get(0).getValue() + "/",textField.getText() + ".atlas");

        FsFile fileDir = new FsFile(textField.getText() + "-temp/", true);
        FileManager.deleteFile(fileDir.getFile());

        if(checkBox.isSelected()) {

            FsFile fileDir2 = new FsFile(textField.getText(),true);
            FileManager.deleteFile(fileDir2.getFile());

        }

    }

    private void createBlockTextureRotated(int id, String name) {

        for(int i = 0; i < 6; i++) {

            if(i == 2 || i == 4 || i == 5) {

                for(int j = 0; j < 4; j++) {

                    Pixmap pixmap = createBlockTextureRotated(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1), j);
                    Texture texture = new Texture(pixmap);
                    if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                    Pixmap pixmap1 = texture.getTextureData().consumePixmap();
                    FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_" + j + ".png");
                    PixmapIO.writePNG(handle, pixmap1);

                }

            } else if(i == 3) {

                Pixmap pixmap = createBlockTextureRotated(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1), 0);
                Texture texture = new Texture(pixmap);
                if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                Pixmap pixmap1 = texture.getTextureData().consumePixmap();
                FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_" + 0 + ".png");
                PixmapIO.writePNG(handle, pixmap1);

                Pixmap pixmap2 = createBlockTextureRotated(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1), 1);
                Texture texture2 = new Texture(pixmap2);
                if (!texture2.getTextureData().isPrepared()) texture2.getTextureData().prepare();
                Pixmap pixmap3 = texture2.getTextureData().consumePixmap();
                FileHandle handle2 = Gdx.files.local(name + "-temp/" + name + "_" + i + "_" + 1 + ".png");
                PixmapIO.writePNG(handle2, pixmap3);

            } else {

                Pixmap pixmap = createBlockTextureRotated(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1), 0);
                Texture texture = new Texture(pixmap);
                if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                Pixmap pixmap1 = texture.getTextureData().consumePixmap();
                FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_" + 0 + ".png");
                PixmapIO.writePNG(handle, pixmap1);

            }

        }

        Pixmap pixmap = createBlockTexture(6, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1));
        FileHandle handle1 = Gdx.files.local(Paths.ITEM_PATH.getPath() + id + "/" + name + " Icon.png");
        PixmapIO.writePNG(handle1, pixmap);

        FileHandle handle2 = Gdx.files.local(Paths.RES_FOLDER.getPath() + "/itemColor/" + id + "/color.txt");
        FileWriter writer = new FileWriter(handle2,true);
        writer.writeVector3(new Vector3(color.getRed(), color.getGreen(), color.getBlue()));
        writer.nextLine();
        writer.writeBoolean(checkBox.isSelected());
        writer.close();

    }

    private void createBlockTexture(int id, String name) {

        for(int i = 0; i < 6; i++) {

            Pixmap pixmap = createBlockTexture(i, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1));
            FileHandle handle = Gdx.files.local(name + "/" + name + "_" + i + "_0.png");
            if(!handle.exists()) PixmapIO.writePNG(handle, pixmap);

        }

        for(int i = 0; i < 6; i++) {

            if(i == 2 || i == 4 || i == 5) {

                for(int j = 0; j < 4; j++) {

                    if(j == 0) {

                        Texture texture = new Texture(Gdx.files.local(name + "/" + name + "_" + i + "_0.png"));
                        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                        Pixmap pixmap = texture.getTextureData().consumePixmap();
                        FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_0.png");
                        PixmapIO.writePNG(handle, pixmap);

                    } else {

                        Texture texture = RessourceManager.loadTexture(name + "-temp/" + name + "_" + i + "_" + (j - 1) + ".png");
                        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                        Pixmap pixmap = texture.getTextureData().consumePixmap();
                        pixmap = rotatePixmap(pixmap);
                        FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_" + j + ".png");
                        PixmapIO.writePNG(handle, pixmap);

                    }

                }

            } else if(i == 3) {

                Texture texture = RessourceManager.loadTexture(name + "/" + name + "_3_0.png");
                if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                Pixmap pixmap = texture.getTextureData().consumePixmap();
                FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_3_0.png");
                PixmapIO.writePNG(handle, pixmap);

                Texture texture1 = RessourceManager.loadTexture(name + "/" + name + "_3_0.png");
                if (!texture1.getTextureData().isPrepared()) texture1.getTextureData().prepare();
                Pixmap pixmap1 = texture1.getTextureData().consumePixmap();
                pixmap1 = rotatePixmap(pixmap1);
                FileHandle handle1 = Gdx.files.local(name + "-temp/" + name + "_3_1.png");
                PixmapIO.writePNG(handle1, pixmap1);

            } else {

                Texture texture = RessourceManager.loadTexture(name + "/" + name + "_" + i + "_0.png");
                if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
                Pixmap pixmap = texture.getTextureData().consumePixmap();
                FileHandle handle = Gdx.files.local(name + "-temp/" + name + "_" + i + "_0.png");
                PixmapIO.writePNG(handle, pixmap);

            }

        }

        Pixmap pixmap = createBlockTexture(6, name, new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,1));
        FileHandle handle1 = Gdx.files.local(Paths.ITEM_PATH.getPath() + id + "/" + name + " Icon.png");
        PixmapIO.writePNG(handle1, pixmap);

        FileHandle handle2 = Gdx.files.local(Paths.RES_FOLDER.getPath() + "/itemColor/" + id + "/color.txt");
        FileWriter writer = new FileWriter(handle2,true);
        writer.writeVector3(new Vector3(color.getRed(), color.getGreen(), color.getBlue()));
        writer.nextLine();
        writer.writeBoolean(checkBox.isSelected());
        writer.close();

    }

    private Pixmap createBlockTextureRotated(int id, String textureName, Color outlineColor, int rotate) {

        Texture texture = new Texture(Gdx.files.internal("template/Sprite-Template-" + id + ".png"));
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        Pixmap templatePix = texture.getTextureData().consumePixmap();

        for(int i = 0; i < rotate; i++) templatePix = rotatePixmap(templatePix);

        Texture baseTexture = new Texture(Gdx.files.internal(textureName + ".png"));
        if (!baseTexture.getTextureData().isPrepared()) baseTexture.getTextureData().prepare();
        Pixmap texturePix = baseTexture.getTextureData().consumePixmap();

        Pixmap spritePix = new Pixmap(templatePix.getWidth(), templatePix.getHeight(), Pixmap.Format.RGBA8888);

        for(int i = 0; i < templatePix.getWidth(); i++) {

            for(int j = 0; j < templatePix.getHeight(); j++) {

                int templateColor = templatePix.getPixel(i, j), outLineColor = Color.rgba8888(outlineColor);

                if(templateColor == Color.rgba8888(0,0,1,1)) spritePix.drawPixel(i, j, texturePix.getPixel(i, j));
                if(templateColor == Color.rgba8888(0,1,0,1)) spritePix.drawPixel(i, j, outLineColor);

            }

        }

        return spritePix;

    }

    private Pixmap createBlockTexture(int id, String textureName, Color outlineColor) {

        Texture texture = new Texture(Gdx.files.internal("template/Sprite-Template-" + id + ".png"));
        if (!texture.getTextureData().isPrepared()) texture.getTextureData().prepare();
        Pixmap templatePix = texture.getTextureData().consumePixmap();

        Texture baseTexture = new Texture(Gdx.files.internal(textureName + ".png"));
        if (!baseTexture.getTextureData().isPrepared()) baseTexture.getTextureData().prepare();
        Pixmap texturePix = baseTexture.getTextureData().consumePixmap();

        Pixmap spritePix = new Pixmap(templatePix.getWidth(), templatePix.getHeight(), Pixmap.Format.RGBA8888);

        for(int i = 0; i < templatePix.getWidth(); i++) {

            for(int j = 0; j < templatePix.getHeight(); j++) {

                int templateColor = templatePix.getPixel(i, j), outLineColor = Color.rgba8888(outlineColor);

                if(templateColor == Color.rgba8888(0,0,1,1)) spritePix.drawPixel(i, j, texturePix.getPixel(i, j));
                if(templateColor == Color.rgba8888(0,1,0,1)) spritePix.drawPixel(i, j, outLineColor);

            }

        }

        return spritePix;

    }

    private Pixmap rotatePixmap(Pixmap srcPix) {

        int width = srcPix.getWidth();
        int height = srcPix.getHeight();
        Pixmap rotatedPix = new Pixmap(width, height, srcPix.getFormat());
        for(int y = 0; y < width; y++) for(int x = 0; x < height; x++) rotatedPix.drawPixel(height-y-1, x, srcPix.getPixel(x, y));
        return rotatedPix;

    }

}
