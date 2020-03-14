package de.undefinedhuman.sandboxgame.engine.items.type.blocks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

public class Block extends Item {

    public Setting
            durability = new Setting(SettingType.Int, "Durability", 0),
            dropID = new Setting(SettingType.Int, "Drop ID", 0),
            unbreakable = new Setting(SettingType.Bool, "Unbreakable", false),
            collide = new Setting(SettingType.Bool, "Collide", false),
            hasStates = new Setting(SettingType.Bool, "Has States", true),
            isFull = new Setting(SettingType.Bool, "Is Full", false),
            canBePlacedInBackLayer = new Setting(SettingType.Bool, "Back Layer", false),
            needBack = new Setting(SettingType.Bool, "Need Back", false),
            atlasName = new Setting(SettingType.Bool, "Atlas Name", "Dirt");

    private String atlasPath;
    public TextureRegion[][] blockTextures;
    private TextureAtlas textureAtlas;

    public Block() {
        super();
        settings.addSettings(durability, dropID, unbreakable, collide, hasStates, isFull, canBePlacedInBackLayer, needBack, atlasName);
    }

    @Override
    public String[] getTextures() {
        this.atlasPath = Paths.ITEM_PATH.getPath() + id + "/" + atlasName + ".atlas";
        this.iconTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + atlasName + " Icon.png");
        this.previewTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + atlasName + " Icon.png");
        return new String[] { atlasPath, iconTexture.getString(), previewTexture.getString() };
    }

    public void init() {
        textureAtlas = new TextureAtlas(atlasPath);
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 4; j++) blockTextures[i][j] = textureAtlas.findRegion(atlasName + "_" + i + "_" + j);
    }

    @Override
    public void delete() {
        textureAtlas.dispose();
    }

}
