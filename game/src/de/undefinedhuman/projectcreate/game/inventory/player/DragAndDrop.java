package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.SelectItemPacket;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.MouseConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.utils.Mouse;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.inventory.InvTarget;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;
import de.undefinedhuman.projectcreate.game.item.drop.DropItemManager;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;

public class DragAndDrop {

    private ArrayList<InvTarget> targets;

    private boolean half = false, moving = false, isLeft = false, alreadyClicked = false;
    private InvSlot lastSlot = null;
    private Vector2i currentItem = new Vector2i() {
        @Override
        public Vector2i set(int x, int y) {
            Vector2i vector = super.set(x, y);
            if(draggableIcon == null || amountText == null)
                return vector;
            draggableIcon.setTexture(ItemManager.getInstance().getItem(x).iconTexture.getValue());
            amountText.setText(y == 1 ? "" : y);
            draggableIcon.resize();
            return vector;
        }
    };
    private Gui draggableIcon;
    private Text amountText;

    public DragAndDrop(OrthographicCamera camera) {
        targets = new ArrayList<>();
        draggableIcon = new Gui(Variables.DEFAULT_TEXTURE);
        draggableIcon.set(new MouseConstraint(camera), new MouseConstraint(camera), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE));
        draggableIcon.setOffset(new CenterOffset(), new CenterOffset());
        draggableIcon.addChild(
                amountText = (Text) new Text(0)
                        .setFontSize(8)
                        .setPosition(new RelativeConstraint(1.2f), new RelativeConstraint(0)).setOffsetX(new RelativeOffset(-1f))
        );
    }

    public void update(OrthographicCamera camera) {

        boolean isLeftClicked = Mouse.isLeftClicked(), isRightClicked = Mouse.isRightClicked();

        if (InventoryManager.getInstance().isInventoryOpened()) {

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

        } else if(moving) interruptMoving(camera);

    }

    private void placeOneItem(OrthographicCamera camera) {
        InvSlot clickedSlot = getClickedSlot(camera);
        if (clickedSlot == null || (!clickedSlot.isEmpty() && !clickedSlot.isTypeCompatible(currentItem.x)) || clickedSlot.addItem(currentItem.x, 1) != 0)
            return;
        currentItem.set(currentItem.x, currentItem.y-1);
        if (currentItem.y < 1)
            cancelMoving();
    }

    private void startMoving(boolean isLeftClicked, OrthographicCamera camera) {
        InvSlot clickedSlot = getClickedSlot(camera);
        if(clickedSlot == null || clickedSlot.isEmpty())
            return;
        half = !isLeftClicked;
        ClientManager.getInstance().sendTCP(SelectItemPacket.serialize(clickedSlot.getLinkedEntityID(), clickedSlot.getLinkInventoryName(), clickedSlot.getRow(), clickedSlot.getCol(), half));
        int amount = clickedSlot.getAmount() / (half ? 2 : 1);
        currentItem.set(clickedSlot.getID(), amount);
        clickedSlot.removeItem(amount);
        setMoving(clickedSlot);
    }

    private void interruptMoving(OrthographicCamera camera) {
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
    }

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

    private void removeTempItem() {
        currentItem.setZero();
        half = false;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!moving || currentItem.x == 0 || currentItem.y == 0)
            return;
        draggableIcon.resize();
        draggableIcon.render(batch, camera);
    }

    public void resize(int width, int height) {
        draggableIcon.resize(width, height);
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
