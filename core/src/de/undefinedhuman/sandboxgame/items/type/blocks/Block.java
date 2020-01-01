package de.undefinedhuman.sandboxgame.items.type.blocks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.ressources.SoundManager;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.items.Item;
import de.undefinedhuman.sandboxgame.items.ItemType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Block extends Item {

    public int durability, dropID;
    public boolean unbreakable, collide, fluid, animated, hasLight, hasStates, isFull, canBePlacedInBackLayer, needBack;
    public Vector3 lightColor;

    public String atlasName, soundName;

    public TextureRegion[][] blockTextures;
    private TextureAtlas textureAtlas;
    private String atlasPath;

    public Block() {
        this.durability = 0;
        this.dropID = 0;
        this.unbreakable = false;
        this.collide = false;
        this.fluid = false;
        this.animated = false;
        this.hasLight = false;
        this.hasStates = true;
        this.isFull = true;
        this.needBack = false;
        this.canBePlacedInBackLayer = false;
        this.lightColor = new Vector3();
        this.useIconAsTexture = true;

        this.blockTextures = new TextureRegion[6][4];
        this.atlasName = "Dirt";
        this.atlasPath = "Dirt.atlas";
        this.soundName = "dirtSound.wav";
        this.type = ItemType.BLOCK;
    }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {

        super.load(id, settings);
        if(settings.containsKey("Atlas")) atlasName = settings.get("Atlas").getNextString();
        atlasPath = Paths.ITEM_PATH.getPath() + id + "/" + atlasName + ".atlas";
        iconTexture = Paths.ITEM_PATH.getPath() + id + "/" + atlasName + " Icon.png";
        TextureManager.instance.addTexture(iconTexture);

        if(settings.containsKey("Sound")) soundName = Paths.ITEM_PATH.getPath() + id + "/" + settings.get("Sound").getNextString();
        SoundManager.instance.addSound(soundName);

        durability = Tools.loadInt(settings,"Durability",0);
        dropID = Tools.loadInt(settings,"DropID", id);
        unbreakable = Tools.loadBoolean(settings,"Unbreakable",false);
        collide = Tools.loadBoolean(settings,"Collide",true);
        fluid = Tools.loadBoolean(settings,"Fluid",false);
        animated = Tools.loadBoolean(settings,"Animated",false);
        hasLight = Tools.loadBoolean(settings,"HasLight",false);
        hasStates = Tools.loadBoolean(settings,"HasStates",true);
        isFull = Tools.loadBoolean(settings,"IsFull",true);
        canBePlacedInBackLayer = Tools.loadBoolean(settings,"PlacedInBackL",true);
        lightColor = Tools.loadVector3(settings,"LightColor",new Vector3());
        init();

    }

    public void init() {
        textureAtlas = new TextureAtlas(atlasPath);
        for (int i = 0; i < 6; i++) for (int j = 0; j < 4; j++) blockTextures[i][j] = textureAtlas.findRegion(atlasName + "_" + i + "_" + j);
    }

    @Override
    public void delete() {
        super.delete();
        textureAtlas.dispose();
    }

}
