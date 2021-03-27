package de.undefinedhuman.projectcreate.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.collision.CollisionManager;
import de.undefinedhuman.projectcreate.engine.camera.CameraManager;
import de.undefinedhuman.projectcreate.engine.items.type.blocks.Block;
import de.undefinedhuman.projectcreate.engine.items.type.tools.Pickaxe;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;
import de.undefinedhuman.projectcreate.entity.Entity;
import de.undefinedhuman.projectcreate.entity.EntityManager;
import de.undefinedhuman.projectcreate.inventory.player.Selector;
import de.undefinedhuman.projectcreate.item.ItemManager;
import de.undefinedhuman.projectcreate.item.drop.DropItemManager;
import de.undefinedhuman.projectcreate.network.ClientManager;
import de.undefinedhuman.projectcreate.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.utils.Mouse;
import de.undefinedhuman.projectcreate.utils.Utils;

public class WorldManager {

    public static WorldManager instance;

    private boolean canPlace = true, canDestroy = true;
    private float currentDurability = 0, destroyTime = 0, placeTime = 0, placeDuration = 0.05f;
    private byte oldLayer = -1;
    private int oldPickaxeID = 0;
    private Vector2i oldBreakPos = new Vector2i();

    private static final byte[] collisionMask = new byte[] {
            0, 0, 0, 2, 0, 0, 3, 1, 0, 4, 0, 1, 5, 1, 1, 1
    };

    public void update(float delta) {
        updatePlacementVariables(delta);
        updateDestroyVariables(delta);
    }

    private void updatePlacementVariables(float delta) {
        if (canPlace) return;
        this.placeTime += delta;
        if (placeTime < placeDuration) return;
        canPlace = true;
        placeTime = 0;
    }

    private void updateDestroyVariables(float delta) {
        if (canDestroy) return;
        if (!(Selector.instance.getSelectedItem() instanceof Pickaxe)) {
            setDestroyVariables(-1);
            return;
        }

        Pickaxe pickaxe = (Pickaxe) Selector.instance.getSelectedItem();
        if (oldPickaxeID != pickaxe.id.getInt()) {
            setDestroyVariables(oldPickaxeID);
            return;
        }
        this.destroyTime += delta;
        if (destroyTime > pickaxe.speed.getFloat()) canDestroy = true;
    }

    public void placeBlock(byte worldLayer) {
        if (!canPlace) return;
        int id = Selector.instance.getSelectedItemID();
        Block block = (Block) ItemManager.instance.getItem(id);
        Vector2i blockPos = Utils.convertToBlockPos(Utils.getWorldPos(CameraManager.gameCamera, Mouse.getMouseCoords()));

        if(id == 0 || !isInRange(blockPos, Utils.convertToBlockPos(GameManager.instance.player.getCenterPosition()), Variables.BLOCK_PLACEMENT_RANGE)) return;
        if(worldLayer == World.MAIN_LAYER) placeBlockInMainLayer(blockPos.x, blockPos.y, block);
        if(worldLayer == World.BACK_LAYER) placeBlockInBackLayer(blockPos.x, blockPos.y, block);
    }

    private void placeBlockInMainLayer(int x, int y, Block block) {
        if(World.instance.getBlock(x, y, World.MAIN_LAYER) != 0) return;
        for (Entity entity : EntityManager.instance.getEntitiesWithCollision(new Vector4(x, y, x + Variables.BLOCK_SIZE, y + Variables.BLOCK_SIZE))) {
            if (!CollisionManager.blockCanBePlaced(entity, x, y)) return;
        }
        if(World.instance.getBlock(x, y, World.BACK_LAYER) != 0 || (!block.needBack.getBoolean() && isBlockInRange(x, y, World.MAIN_LAYER))) placeBlock(x, y, World.MAIN_LAYER, block.id.getByte(), true);
    }

    private void placeBlockInBackLayer(int x, int y, Block block) {
        Block mainLayerBlock = getBlock(x, y, World.MAIN_LAYER);
        if(World.instance.getBlock(x, y, World.BACK_LAYER) != 0 || !block.canBePlacedInBackLayer.getBoolean() || (isBlockInRange(x, y, World.BACK_LAYER) || !(mainLayerBlock != null && mainLayerBlock.hasCollision.getBoolean()))) return;
        placeBlock(x, y, World.BACK_LAYER, block.id.getByte(), true);
    }

    public void placeBlock(int x, int y, byte worldLayer, byte blockID, boolean send) {
        World.instance.setBlock(x, y, worldLayer, blockID);
        checkCells(x, y, worldLayer);
        canPlace = false;
        if(!send) return;
        ClientManager.instance.sendTCP(PacketUtils.createBlockPacket(x, y, worldLayer, blockID));
        Selector.instance.getSelectedInvItem().removeItem();
    }

    public void destroyBlock(byte worldLayer) {

        if (!canDestroy) return;

        Pickaxe pickaxe = (Pickaxe) Selector.instance.getSelectedItem();
        Vector2i blockPos = Utils.convertToBlockPos(Utils.getWorldPos(CameraManager.gameCamera, Mouse.getMouseCoords())), playerCenter = Utils.convertToBlockPos(new Vector2().add(GameManager.instance.player.getPosition()).add(GameManager.instance.player.getCenter()));
        Block currentBlock = (Block) ItemManager.instance.getItem(World.instance.getBlock(blockPos.x, blockPos.y, World.MAIN_LAYER));

        if (currentBlock.id.getInt() == 0 || !isInRange(blockPos, playerCenter, pickaxe.range.getInt()) || currentBlock.durability.getInt() == -1)
            return;

        if (oldBreakPos != blockPos || oldLayer != worldLayer) {
            oldBreakPos = blockPos;
            currentDurability = currentBlock.durability.getFloat();
            oldLayer = worldLayer;
        }

        currentDurability -= 200; // TODO pickaxe.strength;
        if (currentDurability > 0) hurtBlock();
        else destroyBlock(blockPos.x, blockPos.y, worldLayer, true);
        // TODO canDestroy = false;

    }

    public void hurtBlock() { }

    public void destroyBlock(int x, int y, byte worldLayer, boolean send) {
        if(World.instance.getBlock(x, y, worldLayer) == 0) return;
        DropItemManager.instance.addDropItem(World.instance.getBlock(x, y, worldLayer), 1, new Vector2(x, y).scl(Variables.BLOCK_SIZE).add(Variables.BLOCK_SIZE*0.5f, Variables.BLOCK_SIZE*0.5f));
        World.instance.setBlock(x, y, worldLayer, (byte) 0);
        checkCells(x, y, worldLayer);
        if(send)
            ClientManager.instance.sendTCP(PacketUtils.createBlockPacket(x, y, worldLayer, (byte) -1));
        if(worldLayer == World.BACK_LAYER && getBlock(x, y, World.MAIN_LAYER).needBack.getBoolean()) {
            destroyBlock(x, y, World.MAIN_LAYER, send);
        }
    }

    private void setDestroyVariables(int pickaxeID) {
        this.currentDurability = (float) 0;
        this.canDestroy = true;
        this.destroyTime = (float) 0;
        this.oldBreakPos.set(-1, -1);
        this.oldPickaxeID = pickaxeID;
    }

    public void checkMap(byte worldLayer) {
        for (int x = 0; x < World.instance.size.x; x++)
            for (int y = 0; y < World.instance.size.y; y++)
                    checkCell(x, y, worldLayer);

        for(int i = 0; i < World.instance.collisionSize.x; i++)
            for(int j = 0; j < World.instance.collisionSize.y; j++)
                checkCollisionCell(i, j);
    }

    public void checkCells(int x, int y, byte worldLayer) {
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                checkCell(x + i, y + j, worldLayer);

        for (int i = -3; i <= 4; i++)
            for (int j = -3; j <= 4; j++)
                checkCollisionCell(x*2+i, y*2+j);
    }

    private void checkCell(int x, int y, byte worldLayer) {
        if(isTransparent(World.instance.getBlock(x, y, worldLayer))) return;
        World.instance.setState(x, y, worldLayer, (byte) (isSolid(x-1, y, worldLayer, (byte) 1) + isSolid(x, y+1, worldLayer, (byte) 2) + isSolid(x+1, y, worldLayer, (byte) 4) + isSolid(x, y-1, worldLayer, (byte) 8)));
    }

    public void checkCollisionBlock(int x, int y) {
        for(int j = -1; j <= 2; j++)
            for(int i = -1; i <= 2; i++)
                checkCollisionCell(x*2 + i, y*2 + j);
    }

    public void checkCollisionCell(int x, int y) {
        World.instance.setCollision(x, y, World.COLLISION_STATE_LAYER, collisionMask[getCollisionValue(x-1, y, (byte) 1) + getCollisionValue(x, y+1, (byte) 2) + getCollisionValue(x+1, y, (byte) 4) + getCollisionValue(x, y-1, (byte) 8)]);
    }

    private byte getCollisionValue(int x, int y, byte value) {
        return World.instance.getCollision(x, y, World.COLLISION_BASE_LAYER) != 0 ? value : 0;
    }

    public byte isSolid(int x, int y, byte worldLayer, byte value) {
        Block block = getBlock(x, y, worldLayer);
        return (block != null && block.id.getInt() != 0 && block.hasCollision.getBoolean()) ? value : 0;
    }

    public boolean isTransparent(byte blockID) {
        return blockID == 0 || !((Block) ItemManager.instance.getItem(blockID)).isFull.getBoolean();
    }

    private boolean isInRange(Vector2i position1, Vector2i position2, int range) {
        return Math.abs(position1.x - position2.x) <= range && Math.abs(position1.y - position2.y) <= range;
    }

    private Block getBlock(int x, int y, byte worldLayer) {
        return (Block) ItemManager.instance.getItem(World.instance.getBlock(x, y, worldLayer));
    }

    public boolean isBlockInRange(int x, int y, byte worldLayer) {
        return hasCollision(x, y-1, worldLayer) || hasCollision(x-1, y, worldLayer) || hasCollision(x+1, y, worldLayer) || hasCollision(x, y+1, worldLayer);
    }

    private boolean hasCollision(int x, int y, byte worldLayer) {
        return ((Block) ItemManager.instance.getItem(World.instance.getBlock(x, y, worldLayer))).hasCollision.getBoolean();
    }

}
