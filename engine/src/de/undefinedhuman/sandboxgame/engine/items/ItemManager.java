package de.undefinedhuman.sandboxgame.engine.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.arm.ShoulderComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.projectiles.Projectile;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.world.WorldManager;

import java.util.HashMap;

public class ItemManager extends Manager {

    public static ItemManager instance;

    private HashMap<Integer, Item> items;

    public ItemManager() {
        if (instance == null) instance = this;
        this.items = new HashMap<>();
    }

    @Override
    public void init() {
        super.init();
        addItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13);
    }

    public void addItems(int... id) {
        for (int i : id) addItem(i);
    }

    public boolean addItem(int id) {
        if (!hasItem(id) && ResourceManager.existItem(id)) items.put(id, ResourceManager.loadItem(id));
        return hasItem(id);
    }

    public boolean hasItem(int id) {
        return items.containsKey(id);
    }

    @Override
    public void delete() {
        for (Item item : items.values()) item.delete();
        items.clear();
    }

    public void removeItems(int... id) {
        for (int i : id) removeItem(i);
    }

    public void removeItem(int id) {
        if (hasItem(id)) {
            items.get(id).delete();
            items.remove(id);
        }
    }

    public Item getItem(int id) {
        if (hasItem(id)) return items.get(id);
        else if (addItem(id)) return items.get(id);
        return hasItem(0) ? getItem(0) : null;
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public void useItem(int id) {

        Item item = ItemManager.instance.getItem(id);

        if (item != null) {

            boolean mouseLeft = Gdx.input.isButtonPressed(0), mouseRight = Gdx.input.isButtonPressed(1);

            Entity player = GameManager.instance.player;

            if (InventoryManager.instance.canUseItem()) {

                switch (item.type) {

                    case BLOCK:
                        if (mouseLeft || mouseRight) WorldManager.instance.placeBlock(mouseLeft);
                        break;
                    case PICKAXE:
                        if (mouseLeft || mouseRight) WorldManager.instance.destroyBlock(mouseLeft);
                        break;
                    case BOW:
                        if (mouseLeft) {
                            AnimationComponent animationComponent = (AnimationComponent) player.getComponent(ComponentType.ANIMATION);
                            EquipComponent equipComponent = (EquipComponent) player.getComponent(ComponentType.EQUIP);
                            ShoulderComponent shoulderComponent = (ShoulderComponent) player.getComponent(ComponentType.SHOULDER);
                            AngleComponent angleComponent = (AngleComponent) player.getComponent(ComponentType.ANGLE);
                            Vector2 weaponPosition = equipComponent.getCurrentPosition(animationComponent.getCurrentAnimationFrameID()), shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getCurrentAnimationFrameID()),
                                    shoulderOffset = shoulderComponent.getShoulderOffset(animationComponent.getCurrentAnimationFrameID());

                            /*Vector2 launcherOffset = new Vector2(weaponPosition).sub(shoulderPosition);
                            launcherOffset.x = Math.abs(launcherOffset.x);
                            launcherOffset.y = Math.abs(launcherOffset.y);
                            launcherOffset.setAngle(angleComponent.isTurned ? (angleComponent.angle % 360) : (angleComponent.angle % 360));

                            Vector2 cannonCoord = new Vector2(player.getPosition()).add(shoulderPosition).sub(launcherOffset);
                            Vector2 shoulderToCannonCoord = new Vector2(cannonCoord).sub(shoulderPosition);
                            shoulderToCannonCoord.setLength(100);*/

                            Vector2 cannonCoord = new Vector2(weaponPosition).sub(shoulderPosition);
                            cannonCoord.setAngle(((angleComponent.angle + 251) % 360) + (angleComponent.isTurned ? 0 : 39.5f));
                            Vector2 spawnPosition = new Vector2(player.transform.getPosition()).add(shoulderPosition).add((angleComponent.isTurned ? 0 : 12), 0).sub(0, 2).add(cannonCoord);

                            Vector2 angle = new Vector2(weaponPosition).sub(shoulderPosition);
                            angle.setAngle((angleComponent.angle + 270) % 360);
                            angle.setLength(100);
                            //angle.setAngle((angleComponent.angle + 250) % 360);

                            GameManager.instance.projectile = new Projectile(player, spawnPosition, angle.x * 7, angle.y * 7);

                        }
                        break;

                }

            }

        }

    }

}
