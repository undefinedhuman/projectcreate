package de.undefinedhuman.sandboxgame.items;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Item {

    // TODO Rework the whole setting thing with setting class just like the player settings like window width and height -
    //  Make it so that the editor can automatically create the settings and save them and the game can automatically can load them.

    public int id, maxAmount;
    public String name, desc;
    public ItemType type;
    public boolean isStackable, canShake;

    public String itemTexture, iconTexture, inspectTexture;

    public Rarity rarity;

    public boolean useIconAsTexture = false;
    public Vector2 hitboxSize = new Vector2();

    public Item() {

        this.name = "Unknown";
        this.desc = "Unknown";
        this.type = ItemType.ITEM;
        this.iconTexture = "Unknown.png";
        this.itemTexture = "Unknown.png";
        this.inspectTexture = "Unknown.png";
        this.isStackable = true;
        this.maxAmount = 999;
        this.canShake = true;
        this.rarity = Rarity.RARE;

    }

    public void load(int id, HashMap<String, LineSplitter> settings) {

        this.id = id;

        if (type != ItemType.BLOCK) {
            itemTexture = Paths.ITEM_PATH.getPath() + id + "/" + Tools.loadString(settings, "Texture", "Unknown.png");
            iconTexture = Paths.ITEM_PATH.getPath() + id + "/" + Tools.loadString(settings, "Icon", "Unknown.png");
            TextureManager.instance.addTexture(itemTexture, iconTexture);
        }

        name = Tools.loadString(settings, "Name", "");
        desc = Tools.loadString(settings, "Description", "");
        isStackable = Tools.loadBoolean(settings, "Stackable", false);
        maxAmount = Tools.loadInt(settings, "MaxAmount", 0);
        canShake = Tools.loadBoolean(settings, "Shake", false);

    }

    public void delete() {
        TextureManager.instance.removeTexture(itemTexture, iconTexture, inspectTexture);
    }

}
