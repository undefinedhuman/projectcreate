package de.undefinedhuman.sandboxgame.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.collision.CollisionManager;
import de.undefinedhuman.sandboxgame.engine.camera.CameraManager;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.BlockType;
import de.undefinedhuman.sandboxgame.engine.items.type.tools.Pickaxe;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.item.drop.DropItemManager;
import de.undefinedhuman.sandboxgame.network.ClientManager;
import de.undefinedhuman.sandboxgame.network.utils.PacketUtils;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Mouse;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class WorldManager {

    public static WorldManager instance;

    // TODO Refactor ABER GANZ DRINGEND

    private boolean canPlace, canDestroy, oldLayer;
    private float currentDurability = 0, destroyTime = 0, placeTime = 0, placeDuration = 0.05f;
    private int oldPickaxeID = 0;
    private Vector2 oldBreakPos;

    private HashMap<Byte, byte[]> slopeHeights = new HashMap<>();

    public WorldManager() {
        canPlace = true;
        canDestroy = true;
        oldBreakPos = null;
        slopeHeights.put((byte) 4, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
        slopeHeights.put((byte) 1, new byte[] { 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 });
        slopeHeights.put((byte) 0, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 8, 7, 6, 5, 4, 3, 2, 1 });
        slopeHeights.put((byte) 8, new byte[] { 1, 3, 5, 7, 9, 11, 13, 15, 15, 13, 11, 9, 7, 5, 3, 1 });
        slopeHeights.put((byte) 9, new byte[] { 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 });
        slopeHeights.put((byte) 12, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    }

    public void update(float delta) {

        if (!canPlace) {

            this.placeTime += delta;
            if (placeTime >= placeDuration) {
                canPlace = true;
                placeTime = 0;
            }

        }

        if (!canDestroy) {

            if (InventoryManager.instance.getSelector().getSelectedItem() instanceof Pickaxe) {

                Pickaxe pickaxe = (Pickaxe) InventoryManager.instance.getSelector().getSelectedItem();
                if (oldPickaxeID == pickaxe.id.getInt()) {

                    this.destroyTime += delta;
                    if (destroyTime > pickaxe.speed.getFloat()) canDestroy = true;

                } else setDestroyParameter(oldPickaxeID);

            } else setDestroyParameter(-1);

        }

    }

    private void setDestroyParameter(int pickaxeID) {

        this.currentDurability = (float) 0;
        this.canDestroy = true;
        this.destroyTime = (float) 0;
        this.oldBreakPos.set(-1, -1);
        this.oldPickaxeID = pickaxeID;

    }

    public void placeBlock(boolean main) {

        // TODO Rework this one, especially the collision thing

        if (canPlace) {

            int id = InventoryManager.instance.getSelector().getSelectedItemID();
            Block block = (Block) ItemManager.instance.getItem(id);
            // TODO Look into playercenter
            Vector2 blockPos = Tools.convertToWorldCoords(Tools.getWorldPos(CameraManager.gameCamera, Mouse.getMouseCoords())),
                    playerCenter = Tools.convertToWorldCoords(GameManager.instance.player.getCenterPosition());

            Block currentBlock = (Block) ItemManager.instance.getItem(getBlock(true, blockPos));

            if (id != 0 && isInRange(blockPos, playerCenter, 6)) {

                if (main) {

                    if (currentBlock.id.getInt() == 0) {

                        boolean canBePlaced = true;
                        for (Entity entity : EntityManager.instance.getEntitiesWithCollision(Tools.calculateBounds(blockPos, new Vector2(0, 0), new Vector2(Variables.BLOCK_SIZE, Variables.BLOCK_SIZE))))
                            if (!CollisionManager.blockCanBePlaced(entity, blockPos, (byte) id)) {
                                canBePlaced = false;
                                break;
                            }

                        if (canBePlaced) {

                            if (block.needBack.getBoolean() && CollisionManager.collide(getBlock(false, blockPos)))
                                placeBlock(true, blockPos, (byte) id, true);
                            if (!block.needBack.getBoolean() && (isBlockInRange(true, blockPos) || CollisionManager.collide(getBlock(false, blockPos))))
                                placeBlock(true, blockPos, (byte) id, true);

                        }

                    }

                } else if (getBlock(false, blockPos) == 0 && block.canBePlacedInBackLayer.getBoolean() && (isBlockInRange(false, blockPos) || (currentBlock.id.getInt() != 0 && currentBlock.blockType.getBlockType() != BlockType.Empty)))
                    placeBlock(false, blockPos, (byte) id, true);

            }

        }

    }

    public byte isSlope(float worldX, int blockY, byte[] slopes) {
        int blockX = (int) (worldX / Variables.BLOCK_SIZE);
        if(getBlock(blockX, blockY) == 0) return -1;
        byte currentState = World.instance.mainLayer.getState(blockX, blockY);
        if(!slopeHeights.containsKey(currentState)) return -1;
        for(byte slope : slopes) if(slope == currentState) return slopeHeights.get(currentState)[(int) (worldX%Variables.BLOCK_SIZE)];
        return -1;
    }

    public boolean isBlock(int x, int y) {
        return getBlockType(x, y) == BlockType.Block;
    }

    public boolean isObstacleWithSlope(int x, int y) {
        return getBlockType(x, y) == BlockType.Block;
    }

    public boolean isObstacleWithOutSlope(int x, int y) {
        return getBlockType(x, y) == BlockType.Block && !slopeHeights.containsKey(World.instance.mainLayer.getState(x, y));
    }

    public BlockType getBlockType(int x, int y) {
        if(y < 0 || y >= World.instance.height) return BlockType.Block;
        return ((Block) ItemManager.instance.getItem(getBlock(x, y))).blockType.getBlockType();
    }

    private byte getBlock(int x, int y) {
        return World.instance.mainLayer.getBlock(x, y);
    }

    private byte getBlock(boolean main, Vector2 pos) {
        return main ? World.instance.mainLayer.getBlock((int) pos.x, (int) pos.y) : World.instance.backLayer.getBlock((int) pos.x, (int) pos.y);
    }

    private boolean isInRange(Vector2 pos1, Vector2 pos2, int range) {
        return Math.abs(pos1.x - pos2.x) <= range && Math.abs(pos1.y - pos2.y) <= range;
    }

    public void placeBlock(boolean main, Vector2 blockPos, byte id, boolean send) {

        setBlock(main, blockPos, id);
        if (send) ClientManager.instance.sendTCP(PacketUtils.createBlockPacket(id, blockPos.x, blockPos.y, main));
        if (send) InventoryManager.instance.getSelector().getSelectedInvItem().removeItem();
        check3Cells(main ? World.instance.mainLayer : World.instance.backLayer, (int) blockPos.x, (int) blockPos.y);
        canPlace = false;

    }

    public boolean isBlockInRange(boolean main, Vector2 position) {
        byte below = getBlock(main, new Vector2(0, -1).add(position)), above = getBlock(main, new Vector2(0, 1).add(position)), left = getBlock(main, new Vector2(-1, 0).add(position)), right = getBlock(main, new Vector2(1, 0).add(position));
        return CollisionManager.collide(below) || CollisionManager.collide(above) || CollisionManager.collide(left) || CollisionManager.collide(right);
    }

    private void setBlock(boolean main, Vector2 pos, byte id) {

        if (main) World.instance.mainLayer.setBlock((int) pos.x, (int) pos.y, id);
        else World.instance.backLayer.setBlock((int) pos.x, (int) pos.y, id);

    }

    public void check3Cells(WorldLayer layer, int x, int y) {
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) checkCell(layer, x + i, y + j);
    }

    private void checkCell(WorldLayer layer, int x, int y) {
        if(isTransparent(layer.getBlock(x, y))) return;
        layer.setState(x, y, isTransparent(layer, x-1, y, 1) +
                             isTransparent(layer, x, y+1, 2) +
                             isTransparent(layer, x+1, y, 4) +
                             isTransparent(layer, x, y-1, 8));
    }

    public int isTransparent(WorldLayer layer, int x, int y, int value) {
        Block block = (Block) ItemManager.instance.getItem(layer.getBlock(x, y));
        return !(block != null && (block.id.getInt() == 0 || !block.isFull.getBoolean())) ? value : 0;
    }

    public void destroyBlock(boolean main) {

        if (canDestroy) {

            Pickaxe pickaxe = (Pickaxe) InventoryManager.instance.getSelector().getSelectedItem();
            Vector2 blockPos = Tools.convertToWorldCoords(Tools.getWorldPos(CameraManager.gameCamera, Mouse.getMouseCoords())), playerCenter = Tools.convertToWorldCoords(new Vector2().add(GameManager.instance.player.getPosition()).add(GameManager.instance.player.getCenter()));
            Block currentBlock = (Block) ItemManager.instance.getItem(getBlock(main, blockPos));

            if (currentBlock.id.getInt() != 0 && isInRange(blockPos, playerCenter, pickaxe.range.getInt()) && currentBlock.durability.getInt() != -1) {

                if ((oldBreakPos == null || oldBreakPos != blockPos) || oldLayer != main) {

                    oldBreakPos = blockPos;
                    currentDurability = currentBlock.durability.getFloat();
                    oldLayer = main;

                }

                currentDurability -= 200; // TODO TEMP

                //currentDurability -= pickaxe.strength;

                if (currentDurability > 0) hurtBlock();
                else destroyBlock(blockPos, main, true);

                //canDestroy = false; // TODO Temp

            }

        }

    }

    public void hurtBlock() { }

    public void destroyBlock(Vector2 blockPos, boolean main, boolean send) {

        DropItemManager.instance.addDropItem(getBlock(main, blockPos), 1, new Vector2(blockPos.x * Variables.BLOCK_SIZE - Variables.BLOCK_SIZE * 0.5f, blockPos.y * Variables.BLOCK_SIZE - Variables.BLOCK_SIZE*0.5f));
        if (send) ClientManager.instance.sendTCP(PacketUtils.createBlockPacket(-1, blockPos.x, blockPos.y, main));
        setBlock(main, blockPos, (byte) 0);
        check3Cells(main ? World.instance.mainLayer : World.instance.backLayer, (int) blockPos.x, (int) blockPos.y);

    }

    public void checkMap(WorldLayer layer) {
        for (int i = 0; i < layer.width; i++)
            for (int j = 0; j < layer.height; j++) if (layer.getBlock(i, j) != 0) checkCell(layer, i, j);
    }

    public boolean isTransparent(byte b) {
        return b == 0 || !((Block) ItemManager.instance.getItem(b)).isFull.getBoolean();
    }

}
