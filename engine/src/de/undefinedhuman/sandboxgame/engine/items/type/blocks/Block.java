package de.undefinedhuman.sandboxgame.engine.items.type.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public class Block extends Item {

    public Setting
            durability = new Setting(SettingType.Int, "Durability", 0),
            dropID = new Setting(SettingType.Int, "DropID", 0),
            unbreakable = new BooleanSetting("Unbreakable", false),
            collide = new BooleanSetting("Collide", false),
            hasStates = new BooleanSetting("HasStates", true),
            isFull = new BooleanSetting("IsFull", true),
            canBePlacedInBackLayer = new BooleanSetting("Place in Back", false),
            needBack = new BooleanSetting("Need Back", false);

    public TextureRegion[] blockTextures;

    public Block() {
        super();
        settings.addSettings(durability, dropID, unbreakable, collide, hasStates, isFull, canBePlacedInBackLayer, needBack);
        useIconAsHandTexture.setValue(true);
    }

    @Override
    public void init() {
        if(id.getInt() == 0) return;
        blockTextures = TextureManager.instance.getTexture(itemTexture.getString()).split(Variables.BLOCK_SIZE, Variables.BLOCK_SIZE)[0];
        for(int i = 0; i < blockTextures.length; i++) blockTextures[i] = Tools.fixBleeding(blockTextures[i]);
    }

}
