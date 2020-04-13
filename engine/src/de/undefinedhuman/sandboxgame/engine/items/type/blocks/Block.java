package de.undefinedhuman.sandboxgame.engine.items.type.blocks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;

public class Block extends Item {

    public Setting
            durability = new Setting(SettingType.Int, "Durability", 0),
            dropID = new Setting(SettingType.Int, "DropID", 0),
            unbreakable = new BooleanSetting("Unbreakable", false),
            collide = new BooleanSetting("Collide", false),
            hasStates = new BooleanSetting("HasStates", true),
            isFull = new BooleanSetting("IsFull", true),
            canBePlacedInBackLayer = new BooleanSetting("Back Layer", false),
            needBack = new BooleanSetting("NeedBack", false),
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
