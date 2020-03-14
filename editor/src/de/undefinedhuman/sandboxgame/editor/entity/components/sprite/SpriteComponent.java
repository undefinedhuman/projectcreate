package de.undefinedhuman.sandboxgame.editor.entity.components.sprite;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.IntSetting;
import me.gentlexd.sandboxeditor.editor.settings.TextureSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.engine.window.Window;
import me.gentlexd.sandboxeditor.entity.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SpriteComponent extends Component {

    private JTextField spriteDataName;
    private JList listPanel;
    private JPanel dataPanel;
    private JScrollPane listScroller;

    private DefaultListModel listModel;
    private JButton addSpriteData, removeSpriteData;

    private HashMap<String, SpriteData> spriteData;
    private String currentSelectedSpriteData;

    private TextureSetting texture;
    private IntSetting renderLevelSetting;
    private Vector2Setting regionSetting;

    public SpriteComponent(JPanel panel, String name) {

        super(panel, name);

        spriteData = new HashMap<>();

        spriteDataName = new JTextField();
        spriteDataName.setBounds(25, 25,150,25);

        listModel = new DefaultListModel();

        listPanel = new JList(listModel);
        listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.addListSelectionListener(e -> {

            if(currentSelectedSpriteData != null && spriteData.containsKey(currentSelectedSpriteData)) {

                spriteData.get(currentSelectedSpriteData).texturePath = texture.getTexturePath1();
                spriteData.get(currentSelectedSpriteData).textureString = texture.getValue().toString();
                spriteData.get(currentSelectedSpriteData).regionSize = regionSetting.getVector();
                spriteData.get(currentSelectedSpriteData).renderLevel = Integer.parseInt(renderLevelSetting.getValue().toString());
                spriteData.get(currentSelectedSpriteData).image = texture.getTexture();

            }

            if(listPanel.getSelectedValue() != null) {

                currentSelectedSpriteData = listPanel.getSelectedValue().toString();

                if(currentSelectedSpriteData != null && spriteData.containsKey(currentSelectedSpriteData)) {

                    texture.setTexture(spriteData.get(currentSelectedSpriteData).texturePath);
                    regionSetting.setValue(spriteData.get(currentSelectedSpriteData).regionSize);
                    renderLevelSetting.setValue(spriteData.get(currentSelectedSpriteData).renderLevel);

                }

            }

        });

        dataPanel = new JPanel(null);
        dataPanel.setBounds(200,25,190,525);
        dataPanel.setBackground(Color.WHITE);
        dataPanel.setOpaque(true);

        listScroller = new JScrollPane(listPanel);
        listScroller.setBounds(25,125,150,425);

        addSpriteData = new JButton("Add");
        addSpriteData.setBounds(25,55,150,25);
        addSpriteData.addActionListener(e -> {

            if(!listModel.contains(spriteDataName.getText())) {

                listModel.addElement(spriteDataName.getText());
                spriteData.put(spriteDataName.getText(), new SpriteData());

            }

        });

        removeSpriteData = new JButton("Remove");
        removeSpriteData.setBounds(25,85,150,25);
        removeSpriteData.addActionListener(e -> {

            if(listPanel.getSelectedValue() != null) {

                if(listModel.contains(listPanel.getSelectedValue().toString())) {

                    spriteData.remove(listPanel.getSelectedValue().toString());
                    listModel.removeElement(listPanel.getSelectedValue().toString());

                }

            }

        });

    }

    @Override
    public void addMenuComponent() {

        settings.clear();

        panel.add(listScroller);
        panel.add(addSpriteData);
        panel.add(removeSpriteData);
        panel.add(spriteDataName);
        panel.add(dataPanel);

        addSetting(texture = new TextureSetting(dataPanel,"Texture","Unknown.png",false));
        addSetting(renderLevelSetting = new IntSetting(dataPanel,"Render-Level",0,false));
        addSetting(regionSetting = new Vector2Setting(dataPanel,"Region-Size", new Vector2(0,0),false));

        super.addMenuComponent();

    }

    @Override
    public void removeMenuComponent() {

        if(currentSelectedSpriteData != null && spriteData.containsKey(currentSelectedSpriteData)) {

            spriteData.get(currentSelectedSpriteData).texturePath = texture.getTexturePath1();
            spriteData.get(currentSelectedSpriteData).textureString = texture.getValue().toString();
            spriteData.get(currentSelectedSpriteData).regionSize = regionSetting.getVector();
            spriteData.get(currentSelectedSpriteData).renderLevel = Integer.parseInt(renderLevelSetting.getValue().toString());
            spriteData.get(currentSelectedSpriteData).image = texture.getTexture();

        }

        currentSelectedSpriteData = null;

        dataPanel.removeAll();

        super.removeMenuComponent();

    }

    @Override
    public void update() { }

    @Override
    public void save(FileWriter writer) {

        writer.nextLine();

        if(currentSelectedSpriteData != null && spriteData.containsKey(currentSelectedSpriteData)) {

            spriteData.get(currentSelectedSpriteData).texturePath = texture.getTexturePath1();
            spriteData.get(currentSelectedSpriteData).textureString = texture.getValue().toString();
            spriteData.get(currentSelectedSpriteData).regionSize = regionSetting.getVector();
            spriteData.get(currentSelectedSpriteData).renderLevel = Integer.parseInt(renderLevelSetting.getValue().toString());
            spriteData.get(currentSelectedSpriteData).image = texture.getTexture();

        }

        writer.writeInt(spriteData.size());

        for(String title : spriteData.keySet()) {

            SpriteData data = spriteData.get(title);

            writer.writeString(title);
            writer.writeString(data.textureString);
            writer.writeInt(data.renderLevel);
            writer.writeVector2(data.regionSize);

            try {

                File file = new File(Window.instance.editor.getPath().getPath() + Window.instance.editor.settings.get(0).getValue() + "/" + data.textureString);
                if(file.createNewFile()) Log.info("Texture: " + file.getName() + " wurde erfolgreich erstellt!");
                ImageIO.write(data.image,"png", file);

            } catch (IOException e) {

                System.out.println(e.getMessage());
                System.exit(3);

            }

        }

        writer.nextLine();

    }

    @Override
    public void load(FileReader reader, int id) {

        spriteData.clear();

        LineSplitter splitter = new LineSplitter(reader.nextLine(),true,";");

        int size = splitter.getNextInt();

        for(int i = 0; i < size; i++) {

            String dataName = splitter.getNextString();
            String textureName = splitter.getNextString();
            int renderLevel = splitter.getNextInt();
            Vector2 regionSize = splitter.getNextVector2();

            SpriteData data = new SpriteData();
            data.texturePath = Window.instance.editor.getPath().getPath() + Window.instance.editor.settings.get(0).getValue() + "/" + textureName;
            data.textureString = textureName;
            data.regionSize = regionSize;
            data.renderLevel = renderLevel;
            try {
                data.image = ImageIO.read(new File(data.texturePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!listModel.contains(dataName)) {

                listModel.addElement(dataName);
                spriteData.put(dataName, data);

            }

        }

    }

    public class SpriteData {

        String textureString = "Unknown.png", texturePath = "./core/assets/Unknown.png";
        int renderLevel = 0;
        Vector2 regionSize = new Vector2(0,0);
        BufferedImage image;

    }

}
