package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.utils.Mouse;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.game.inventory.InvTarget;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;

import java.util.ArrayList;
import java.util.Arrays;

public class DragAndDrop {

    private ArrayList<InvTarget> targets;

    private boolean half = false, moving = false, isLeft = false, alreadyClicked = false;
    private Vector2i currentItem = new Vector2i();

    public DragAndDrop() {
        targets = new ArrayList<>();
    }

    public void update(OrthographicCamera camera) {

        boolean isClicked = false;

        if (InventoryManager.getInstance().isInventoryOpened()) {

            if (moving) {

                isClicked = true;
                if (!alreadyClicked && ((Mouse.isLeftClicked() && (!isLeft) || (isLeft && Mouse.isRightClicked())))) {
                    placeOneItem(camera);
                    alreadyClicked = true;
                }

            }

            if (!isClicked) {
                if (Mouse.isLeftClicked()) {
                    startMoving(false, camera);
                    isLeft = true;
                }
                if (Mouse.isRightClicked()) {
                    startMoving(true, camera);
                    isLeft = false;
                }
            }

            if ((!Mouse.isRightClicked() && isLeft) || (!isLeft && !Mouse.isLeftClicked())) alreadyClicked = false;

            if (moving && ((isLeft && !Mouse.isLeftClicked()) || (!isLeft && !Mouse.isRightClicked()))) {

                InvSlot clickedSlot = null;
                for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
                /*if (clickedSlot != null && clickedSlot.isTypeCompatible(currentItem)) {

                    if (clickedSlot.getInvItem() == null || clickedSlot.getInvItem().getID() == currentItem.getID() || half) {
                        currentItem.setAmount(clickedSlot.addItem(currentItem));
                    } else {
                        InvItem clickedItem = clickedSlot.getInvItem();
                        clickedSlot.setInvItem(currentItem.getID(), currentItem.getAmount());
                        currentItem.setStats(clickedItem.getID(), clickedItem.getAmount());
                    }

                    cancelMoving();

                } else {

                    if (InventoryManager.getInstance().overGui()) cancelMoving();
                    else {
                        DropItemManager.instance.addDropItem((byte) currentItem.getID(), currentItem.getAmount(), Tools.getWorldPos(CameraManager.gameCamera, new Vector2(Mouse.getX() - 8, Mouse.getY() - 8)));
                        removeTempItem();
                    }

                }*/

            }

        } else {

            if (moving) {

                /*InvSlot clickedSlot = null;
                for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
                if (clickedSlot != null && clickedSlot.isTypeCompatible(currentItem))
                    currentItem.setAmount(clickedSlot.addItem(currentItem));
                cancelMoving();*/

            }

        }

    }

    private void placeOneItem(OrthographicCamera camera) {

        InvSlot clickedSlot = null;
        for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
        if (clickedSlot != null && clickedSlot.isTypeCompatible(currentItem.x)) {

            if (clickedSlot.addItem(currentItem.x, 1) == 0) currentItem.y--;
            if (currentItem.y < 1) cancelMoving();

        }

    }

    private void startMoving(boolean right, OrthographicCamera camera) {

        InvSlot clickedSlot = null;
        /*for (InvTarget inventory : targets) if ((clickedSlot = inventory.getClickedSlot(camera)) != null) break;
        if (clickedSlot != null) {

            InvItem clickedItem = clickedSlot.getInvItem();

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

        }*/

    }

    private void cancelMoving() {

        //
        // lastSlot.addItem(currentItem);
        removeTempItem();
    }

    private void removeTempItem() {
        currentItem.setZero();
        moving = false;
        half = false;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        /*if (moving && (currentItem.getID() != 0 && currentItem.getAmount() != 0)) {
            currentItem.render(batch, camera);
        }*/
    }

    public void resize(int width, int height) {
        // currentItem.resize(width, height);
    }

    public void addTargets(InvTarget... targets) {
        Arrays.stream(targets).filter(invTarget -> !hasTarget(invTarget)).forEach(invTarget -> this.targets.add(invTarget));
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
