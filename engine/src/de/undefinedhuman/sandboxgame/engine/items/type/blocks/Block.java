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
            dropID = new Setting(SettingType.Int, "DropID", 0),
            unbreakable = new Setting(SettingType.Bool, "Unbreakable", false),
            collide = new Setting(SettingType.Bool, "Collide", false),
            hasStates = new Setting(SettingType.Bool, "HasStates", true),
            isFull = new Setting(SettingType.Bool, "IsFull", true),
            canBePlacedInBackLayer = new Setting(SettingType.Bool, "Back Layer", false),
            needBack = new Setting(SettingType.Bool, "NeedBack", false),
            atlasName = new Setting(SettingType.String, "Atlas", "Dirt");

    private String atlasPath;
    public TextureRegion[][] blockTextures;
    private TextureAtlas textureAtlas;

    public Block() {
        super();
        settings.addSettings(durability, dropID, unbreakable, collide, hasStates, isFull, canBePlacedInBackLayer, needBack, atlasName);
        blockTextures = new TextureRegion[6][4];
    }

    @Override
    public String[] getTextures() {
        this.atlasPath = Paths.ITEM_PATH.getPath() + id + "/" + atlasName.getString() + ".atlas";
        this.iconTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + atlasName.getString() + " Icon.png");
        this.previewTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + atlasName.getString() + " Icon.png");
        return new String[] { iconTexture.getString(), previewTexture.getString() };
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public void setAtlas(TextureAtlas atlas) {
        this.textureAtlas = atlas;
    }

    @Override
    public void init() {
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 4; j++)
                blockTextures[i][j] = textureAtlas.findRegion(atlasName.getString() + "_" + i + "_" + j);
        // TODO TEMP until all blocks have this setting implemented
        useIconAsHandTexture.setValue(true);
    }

    @Override
    public void delete() {
        textureAtlas.dispose();
    }

}
