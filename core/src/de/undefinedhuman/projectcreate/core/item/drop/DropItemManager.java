package de.undefinedhuman.projectcreate.core.item.drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class DropItemManager {

    public static DropItemManager instance;

    private ArrayList<DropItem> dropItems, dropItemsToRemove;

    public DropItemManager() {

        this.dropItems = new ArrayList<>();
        this.dropItemsToRemove = new ArrayList<>();

    }

    public void addDropItem(byte itemID, int amount, Vector2 position) {
        this.dropItems.add(new DropItem(itemID, amount, position));
    }

    public void update(float delta) {

        for (DropItem dropItem : dropItems) {

            dropItem.update(delta);
            if (dropItem.isDead) dropItemsToRemove.add(dropItem);

        }

        dropItems.removeAll(dropItemsToRemove);
        dropItemsToRemove.clear();

    }

    public void render(SpriteBatch batch) {
        for (DropItem dropItem : dropItems) dropItem.render(batch);
    }

    public ArrayList<DropItem> getDropItems() {
        return dropItems;
    }

    public void delete() {
        this.dropItems.clear();
    }

}