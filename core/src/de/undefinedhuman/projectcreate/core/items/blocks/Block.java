package de.undefinedhuman.projectcreate.core.items.blocks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.projectcreate.core.crafting.RecipeType;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class Block extends Item {

    public BooleanSetting
            hasCollision = new BooleanSetting("Collides", false),
            isFull = new BooleanSetting("IsFull", true),
            hasStates = new BooleanSetting("HasStates", true),
            needBack = new BooleanSetting("Need Back", false);
    public SelectionSetting<BlockType>
            blockType = new SelectionSetting<>("Block Type", BlockType.values(), value -> BlockType.valueOf(String.valueOf(value)), Enum::name);
    public SelectionSetting<PlacementLayer>
            placementLayer = new SelectionSetting<>("Placement Layer", PlacementLayer.values(), value -> PlacementLayer.valueOf(String.valueOf(value)), Enum::name);
    public IntSetting
            durability = new IntSetting("Durability", 0),
            dropID = new IntSetting("DropID", 0);

    public TextureRegion[] blockTextures;
    private TextureAtlas blockTextureAtlas;

    public Block() {
        super();
        this.recipeType = RecipeType.BLOCK;
        addSettings(blockType, durability, dropID, hasStates, hasCollision, isFull, placementLayer, needBack);
        addSettingsGroup(new SettingsGroup("Block", blockType, durability, dropID, hasStates, hasCollision, isFull, placementLayer, needBack));
    }

    @Override
    public void init() {
        if(!hasStates.getValue() || Variables.IS_EDITOR)
            return;
        blockTextures = new AtlasRegion[16];
        for(int i = 0; i < blockTextures.length; i++) blockTextures[i] = blockTextureAtlas.findRegion("" + i);
    }

    @Override
    public void delete() {
        super.delete();
        if(!hasStates.getValue()) return;
        blockTextures = new AtlasRegion[0];
        blockTextureAtlas.dispose();
    }

    @Override
    public void onClick(int buttonIndex) {

    }
}
