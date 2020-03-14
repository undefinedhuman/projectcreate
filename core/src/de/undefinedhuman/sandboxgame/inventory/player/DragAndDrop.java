package de.undefinedhuman.sandboxgame.inventory.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.inventory.InvItem;
import de.undefinedhuman.sandboxgame.inventory.InvTarget;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.inventory.Slot;
import de.undefinedhuman.sandboxgame.item.drop.DropItemManager;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Mouse;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;

public class DragAndDrop {

    private ArrayList<InvTarget> targets;

    private Slot lastSlot;
    private InvItem currentItem;

    private boolean half = false, moving = false, isLeft = false, alreadyClicked = false;

    public DragAndDrop() {

        targets = new ArrayList<>();
        lastSlot = null;
        currentItem = new InvItem(0);
        currentItem.parent = GuiManager.instance.screen;

    }

    public void update(OrthographicCamera camera) {

        boolean leftMouse = Gdx.input.isButtonPressed(0), rightMouse = Gdx.input.isButtonPressed(1), isClicked = false;

        if (InventoryManager.instance.isInventoryOpened()) {

            if (moving) {

                isClicked = true;
                if (!alreadyClicked && ((leftMouse && (!isLeft) || (isLeft && rightMouse)))) {
                    placeOneItem(camera);
                    alreadyClicked = true;
                }

            }

            if (!isClicked) {
                if (leftMouse) {
                    startMoving(false, camera);
                    isLeft = true;
                }
                if (rightMouse) {
                    startMoving(true, camera);
                    isLeft = false;
                }
            }

            if ((!rightMouse && isLeft) || (!isLeft && !leftMouse)) alreadyClicked = false;

            if (moving && ((isLeft && !leftMouse) || (!isLeft && !rightMouse))) {

                Slot clickedSlot = null;
                for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
                if (clickedSlot != null && clickedSlot.isCompatible(currentItem)) {

                    if (clickedSlot.getItem() == null || clickedSlot.getItem().getID() == currentItem.getID() || half) {
                        currentItem.setAmount(clickedSlot.addItem(currentItem));
                    } else {

                        InvItem clickedItem = clickedSlot.getItem();
                        clickedSlot.setItem(currentItem.getID(), currentItem.getAmount());
                        currentItem.setStats(clickedItem.getID(), clickedItem.getAmount());

                    }

                    cancelMoving();

                } else {

                    if (InventoryManager.instance.overGui()) cancelMoving();
                    else {
                        DropItemManager.instance.addDropItem(currentItem.getID(), currentItem.getAmount(), Tools.getWorldPos(GameManager.gameCamera, new Vector2(Mouse.getX() - 8, Mouse.getY() - 8)));
                        removeTempItem();
                    }

                }

            }

        } else {

            if (moving) {

                Slot clickedSlot = null;
                for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
                if (clickedSlot != null && clickedSlot.isCompatible(currentItem))
                    currentItem.setAmount(clickedSlot.addItem(currentItem));
                cancelMoving();

            }

        }

    }

    private void placeOneItem(OrthographicCamera camera) {

        Slot clickedSlot = null;
        for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
        if (clickedSlot != null && clickedSlot.isCompatible(currentItem)) {

            if (clickedSlot.addItem(currentItem.getID(), 1) == 0) {
                currentItem.setAmount(currentItem.getAmount() - 1);
            }
            if (currentItem.getAmount() < 1) currentItem.setStats(0, 0);

        }

    }

    private void startMoving(boolean right, OrthographicCamera camera) {

        Slot clickedSlot = null;
        for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
        if (clickedSlot != null) {

            InvItem clickedItem = clickedSlot.getItem();

            if (clickedItem != null) {

                if (right) {
                    int amount = clickedItem.getAmount() / 2;
                    half = true;
                    currentItem.setStats(clickedItem.getID(), amount);
                    clickedSlot.removeItem(amount);
                } else {
                    half = false;
                    currentItem.setStats(clickedItem.getID(), clickedItem.getAmount());
                    clickedSlot.deleteItem();
                }

                lastSlot = clickedSlot;
                moving = true;

            }

        }

    }

    private void cancelMoving() {

        lastSlot.addItem(currentItem);
        removeTempItem();

    }

    private void removeTempItem() {

        currentItem.setStats(0, 0);
        moving = false;
        half = false;

    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {

        Vector2 mousePosition = Tools.getWorldCoordsOfMouse(camera);

        if (moving && (currentItem.getID() != 0 && currentItem.getAmount() != 0)) {
            currentItem.setPosition((int) (mousePosition.x - currentItem.getScale().x / 2), (int) (mousePosition.y - currentItem.getScale().y / 2));
            currentItem.render(batch, camera);
        }

    }

    public void resize(int width, int height) {
        currentItem.resize(width, height);
    }

    public void addTarget(InvTarget... targets) {
        for (InvTarget target : targets)
            if (!hasTarget(target)) this.targets.add(target);
    }

    public boolean hasTarget(InvTarget inventory) {
        return targets.contains(inventory);
    }

    public void addTarget(InvTarget target) {
        if (!hasTarget(target)) targets.add(target);
    }

    public void removeTarget(InvTarget target) {
        if (hasTarget(target)) targets.remove(target);
    }

    public boolean isMoving() {
        return moving;
    }

}
