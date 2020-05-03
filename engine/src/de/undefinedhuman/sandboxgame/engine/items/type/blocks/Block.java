package de.undefinedhuman.sandboxgame.engine.items.type.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;

public class Block extends Item {

    public Setting
            blockType = new SelectionSetting("Block Type", BlockType.values()),
            durability = new Setting(SettingType.Int, "Durability", 0),
            dropID = new Setting(SettingType.Int, "DropID", 0),
            hasStates = new BooleanSetting("HasStates", true),
            isFull = new BooleanSetting("IsFull", true),
            canBePlacedInBackLayer = new BooleanSetting("Place in Back", false),
            needBack = new BooleanSetting("Need Back", false);

    public TextureRegion[] blockTextures;
    private TextureAtlas blockTextureAtlas;

    public Block() {
        super();
        settings.addSettings(blockType, durability, dropID, hasStates, isFull, canBePlacedInBackLayer, needBack);
    }

    @Override
    public void init() {
        if(!hasStates.getBoolean()) return;
        blockTextureAtlas = new TextureAtlas(Gdx.files.internal("items/Block.atlas"), Gdx.files.internal("items/" + id.getInt() + "/"));
        blockTextures = new AtlasRegion[16];
        for(int i = 0; i < blockTextures.length; i++) blockTextures[i] = blockTextureAtlas.findRegion("" + i);
    }

    @Override
    public void delete() {
        super.delete();
        if(!hasStates.getBoolean()) return;
        blockTextures = new AtlasRegion[0];
        blockTextureAtlas.dispose();
    }

}
