package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.MouseConstraint;
import de.undefinedhuman.projectcreate.engine.utils.Mouse;
import de.undefinedhuman.projectcreate.game.inventory.InvTarget;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.inventory.slot.LinkedInvItem;

import java.util.ArrayList;
import java.util.Arrays;

public class DragAndDrop {

    private ArrayList<InvTarget> targets;

    private boolean half = false, moving = false, isLeft = false, alreadyClicked = false;
    private InvSlot lastSlot = null;
    private LinkedInvItem draggableItem;

    public DragAndDrop(OrthographicCamera camera) {
        targets = new ArrayList<>();
        draggableItem = new LinkedInvItem();
        draggableItem.setPosition(new MouseConstraint(camera), new MouseConstraint(camera));
    }

    public void update(OrthographicCamera camera) {

        boolean isLeftClicked = Mouse.isLeftClicked(), isRightClicked = Mouse.isRightClicked();

        if(isLeftClicked && !moving)
            startMoving(isLeft = true, camera);

        /*if (InventoryManager.getInstance().isInventoryOpened()) {

            if (moving) {
                if (!alreadyClicked && ((isLeftClicked && !isLeft || (isLeft && isRightClicked)))) {
                    placeOneItem(camera);
                    alreadyClicked = true;
                }
                if((isLeft && !isLeftClicked) || (!isLeft && !isRightClicked)) {
                    InvSlot clickedSlot = getClickedSlot(camera);
                    if (clickedSlot != null && clickedSlot.isTypeCompatible(currentItem.x)) {
                        if (clickedSlot.isEmpty() || clickedSlot.getID() == currentItem.x) currentItem.y = clickedSlot.addItem(currentItem.x, currentItem.y);
                        else if(!currentItem.isZero()) {
                            int tempID = currentItem.x, tempAmount = currentItem.y;
                            currentItem.set(clickedSlot.getID(), clickedSlot.getAmount());
                            clickedSlot.deleteItem();
                            clickedSlot.addItem(tempID, tempAmount);
                        }
                        cancelMoving();
                    } else {
                        if (InventoryManager.getInstance().overGui()) cancelMoving();
                        else {
                            DropItemManager.instance.addDropItem((byte) currentItem.x, currentItem.y, Tools.getWorldPos(CameraManager.gameCamera, new Vector2(Mouse.getX() - 8, Mouse.getY() - 8)));
                            removeTempItem();
                        }
                    }
                }
            } else if(Mouse.isLeftClicked() || Mouse.isRightClicked()) startMoving(isLeft = isLeftClicked, camera);

            if ((!isLeftClicked && !isLeft) || (!isRightClicked && isLeft))
                alreadyClicked = false;

            if(moving && ((!isLeftClicked && isLeft) || (!isRightClicked && !isLeft)))
                moving = false;

        } else if(moving) interruptMoving(camera);*/
    }

    /*private void placeOneItem(OrthographicCamera camera) {
        InvSlot clickedSlot = getClickedSlot(camera);
        if (clickedSlot == null || (!clickedSlot.isEmpty() && !clickedSlot.isTypeCompatible(currentItem.x)) || clickedSlot.addItem(currentItem.x, 1) != 0)
            return;
        currentItem.set(currentItem.x, currentItem.y-1);
        if (currentItem.y < 1)
            cancelMoving();
    }*/

    private void startMoving(boolean isLeftClicked, OrthographicCamera camera) {
        InvSlot clickedSlot = getClickedSlot(camera);
        if(clickedSlot == null || clickedSlot.isEmpty())
            return;
        // ClientManager.getInstance().sendTCP(SelectItemPacket.serialize(clickedSlot.getSlotInfo(), !isLeftClicked));
        setMoving(clickedSlot);
    }

    private void cancelMoving() {
        if(!moving)
            return;
        /*lastSlot.addItem(currentItem.x, currentItem.y);
        removeTempItem();*/
    }

    /*private void interruptMoving(OrthographicCamera camera) {
        if(currentItem.isZero())
            return;
        InvSlot clickedSlot = getClickedSlot(camera);
        if (clickedSlot != null && (clickedSlot.isEmpty() || clickedSlot.isTypeCompatible(currentItem.x)))
            currentItem.y = clickedSlot.addItem(currentItem.x, currentItem.y);
        cancelMoving();
    }

    private void cancelMoving() {
        if(currentItem.isZero())
            return;
        lastSlot.addItem(currentItem.x, currentItem.y);
        removeTempItem();
    }*/

    private InvSlot getClickedSlot(OrthographicCamera camera) {
        InvSlot clickedSlot;
        for (InvTarget inventory : targets)
            if ((clickedSlot = inventory.getClickedSlot(camera)) != null)
                return clickedSlot;
        return null;
    }

    public void setMoving(InvSlot clickedSlot) {
        this.lastSlot = clickedSlot;
        this.moving = true;
    }

    /*private void removeTempItem() {
        currentItem.setZero();
        half = false;
    }*/

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!moving || draggableItem.isEmpty())
            return;
        draggableItem.resize();
        draggableItem.render(batch, camera);
    }

    public LinkedInvItem getDraggableItem() {
        return draggableItem;
    }

    public void resize(int width, int height) {
        draggableItem.resize(width, height);
    }

    public void addTarget(InvTarget... targets) {
        Arrays.stream(targets).filter(invTarget -> !hasTarget(invTarget)).forEach(invTarget -> this.targets.add(invTarget));
    }

    public boolean hasTarget(InvTarget inventory) {
        return targets.contains(inventory);
    }

    public void removeTarget(InvTarget target) {
        if (hasTarget(target)) targets.remove(target);
    }

    public boolean isMoving() {
        return moving;
    }

}
