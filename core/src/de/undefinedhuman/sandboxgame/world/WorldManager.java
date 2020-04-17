package de.undefinedhuman.sandboxgame.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.collision.CollisionManager;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
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

public class WorldManager {

    public static WorldManager instance;

    // TODO Refactor

    private boolean canPlace, canDestroy, oldLayer;
    private float currentDurability = 0, destroyTime = 0, placeTime = 0, placeDuration = 0.05f;
    private int oldPickaxeID = 0;
    private Vector2 oldBreakPos;

    public WorldManager() {
        canPlace = true;
        canDestroy = true;
        oldBreakPos = null;
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

        if (canPlace) {

            int id = InventoryManager.instance.getSelector().getSelectedItemID();
            Block block = (Block) ItemManager.instance.getItem(id);
            // TODO Look into playercenter
            Vector2 blockPos = Tools.convertToWorldCoords(Tools.getWorldPos(GameManager.gameCamera, Mouse.getMouseCoords())),
                    playerCenter = Tools.convertToWorldCoords(GameManager.instance.player.getCenterPosition());

            Block currentBlock = (Block) ItemManager.instance.getItem(getBlock(true, blockPos));

            if (id != 0 && isInRange(blockPos, playerCenter, 6)) {

                if (main) {

                    if (currentBlock.id.getInt() == 0) {

                        boolean canBePlaced = true;
                        for (Entity entity : EntityManager.instance.getEntitiesForCollision())
                            if (!CollisionManager.blockCanBePlaced(entity, blockPos, (byte) id)) {
                                canBePlaced = false;
                                break;
                            }

                        if (canBePlaced) {

                            if (block.needBack.getBoolean() && ((Block) ItemManager.instance.getItem(getBlock(false, blockPos))).collide.getBoolean())
                                placeBlock(true, blockPos, (byte) id, true);
                            if (!block.needBack.getBoolean() && (isBlockInRange(true, blockPos) || ((Block) ItemManager.instance.getItem(getBlock(false, blockPos))).collide.getBoolean()))
                                placeBlock(true, blockPos, (byte) id, true);

                        }

                    }

                } else if (getBlock(false, blockPos) == 0 && block.canBePlacedInBackLayer.getBoolean() && (isBlockInRange(false, blockPos) || (currentBlock.id.getInt() != 0 && currentBlock.collide.getBoolean())))
                    placeBlock(false, blockPos, (byte) id, true);

            }

        }

    }

    private int getBlock(boolean main, Vector2 pos) {
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

        boolean blockInRange = false;
        int below = getBlock(main, new Vector2(0, -1).add(position)), above = getBlock(main, new Vector2(0, 1).add(position)), left = getBlock(main, new Vector2(-1, 0).add(position)), right = getBlock(main, new Vector2(1, 0).add(position));
        if (below != 0 || ((Block) ItemManager.instance.getItem(below)).collide.getBoolean()) blockInRange = true;
        if (above != 0 || ((Block) ItemManager.instance.getItem(above)).collide.getBoolean() && !blockInRange) blockInRange = true;
        if (left != 0 || ((Block) ItemManager.instance.getItem(left)).collide.getBoolean() && !blockInRange) blockInRange = true;
        if (right != 0 || ((Block) ItemManager.instance.getItem(right)).collide.getBoolean() && !blockInRange) blockInRange = true;

        return blockInRange;

    }

    private void setBlock(boolean main, Vector2 pos, byte id) {

        if (main) World.instance.mainLayer.setBlock((int) pos.x, (int) pos.y, id);
        else World.instance.backLayer.setBlock((int) pos.x, (int) pos.y, id);

    }

    public void check3Cells(WorldLayer layer, int x, int y) {
        checkCell(layer, x, y);
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) checkCell(layer, x + i, y + j);
    }

    private void checkCell(WorldLayer layer, int x, int y) {

        Block middle = (Block) ItemManager.instance.getItem(layer.getBlock(x, y)), left = (Block) ItemManager.instance.getItem(layer.getBlock(x - 1, y)), right = (Block) ItemManager.instance.getItem(layer.getBlock(x + 1, y)), above = (Block) ItemManager.instance.getItem(layer.getBlock(x, y + 1)), below = (Block) ItemManager.instance.getItem(layer.getBlock(x, y - 1));

        int mask = (isTransparent(left) ? 0b0001 : 0) | (isTransparent(right) ? 0b0010 : 0) | (isTransparent(above) ? 0b0100 : 0) | (isTransparent(below) ? 0b1000 : 0);

        if (!isTransparent(middle)) {

            // Rand Texturen
            if (mask == 0b0100) {
                layer.setState(x, y, 4);
                layer.setRot(x, y, 0);
            }
            if (mask == 0b0010) {
                layer.setState(x, y, 4);
                layer.setRot(x, y, 1);
            }
            if (mask == 0b1000) {
                layer.setState(x, y, 4);
                layer.setRot(x, y, 2);
            }
            if (mask == 0b0001) {
                layer.setState(x, y, 4);
                layer.setRot(x, y, 3);
            }
            // Rand Texturen ENDE

            // Eck Texturen
            if (mask == 0b0101) {
                layer.setState(x, y, 5);
                layer.setRot(x, y, 0);
            }
            if (mask == 0b0110) {
                layer.setState(x, y, 5);
                layer.setRot(x, y, 1);
            }
            if (mask == 0b1010) {
                layer.setState(x, y, 5);
                layer.setRot(x, y, 2);
            }
            if (mask == 0b1001) {
                layer.setState(x, y, 5);
                layer.setRot(x, y, 3);
            }
            // Eck Texturen ENDE

            // 1 Seitige Texturen
            if (mask == 0b0111) {
                layer.setState(x, y, 2);
                layer.setRot(x, y, 0);
            }
            if (mask == 0b1110) {
                layer.setState(x, y, 2);
                layer.setRot(x, y, 1);
            }
            if (mask == 0b1011) {
                layer.setState(x, y, 2);
                layer.setRot(x, y, 2);
            }
            if (mask == 0b1101) {
                layer.setState(x, y, 2);
                layer.setRot(x, y, 3);
            }
            // 1 Seitige Texturen ENDE

            // 2 Seitige Texturen
            if (mask == 0b0011) {
                layer.setState(x, y, 3);
                layer.setRot(x, y, 0);
            }
            if (mask == 0b1100) {
                layer.setState(x, y, 3);
                layer.setRot(x, y, 1);
            }
            // 2 Seitige Texturen ENDE

            if (mask == 0b1111) {
                layer.setState(x, y, 1);
                layer.setRot(x, y, 0);
            }
            if (mask == 0b0000) {
                layer.setState(x, y, 0);
                layer.setRot(x, y, 0);
            }


        }

    }

    public boolean isTransparent(Block b) {
        return b != null && (b.id.getInt() == 0 || !b.isFull.getBoolean());
    }

    public void destroyBlock(boolean main) {

        if (canDestroy) {

            Pickaxe pickaxe = (Pickaxe) InventoryManager.instance.getSelector().getSelectedItem();
            Vector2 blockPos = Tools.convertToWorldCoords(Tools.getWorldPos(GameManager.gameCamera, Mouse.getMouseCoords())), playerCenter = Tools.convertToWorldCoords(new Vector2().add(GameManager.instance.player.getPosition()).add(GameManager.instance.player.getCenter()));
            Block currentBlock = (Block) ItemManager.instance.getItem(getBlock(main, blockPos));

            if (currentBlock.id.getInt() != 0 && isInRange(blockPos, playerCenter, pickaxe.range.getInt()) && !currentBlock.unbreakable.getBoolean()) {

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
        for (int i = 0; i < World.instance.width; i++)
            for (int j = 0; j < World.instance.height; j++) if (layer.getBlock(i, j) != 0) checkCell(layer, i, j);
    }

    public boolean isTransparent(byte b) {
        return b == 0 || !((Block) ItemManager.instance.getItem(b)).isFull.getBoolean();
    }

}
