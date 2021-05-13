package de.undefinedhuman.projectcreate.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.network.packets.player.JumpPacket;
import de.undefinedhuman.projectcreate.game.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

public class Inputs extends Manager implements InputProcessor {

    public static Inputs instance;

    public Inputs() {
        if (instance == null) instance = this;
    }

    @Override
    public boolean keyDown(int keycode) {

        Entity player = GameManager.instance.player;

        switch (keycode) {

            case Input.Keys.A:
                ((MovementComponent) player.getComponent(MovementComponent.class)).move(true, false);
                ClientManager.instance.sendTCP(PacketUtils.createComponentPacket(player, MovementComponent.class));
                break;
            case Input.Keys.D:
                ((MovementComponent) player.getComponent(MovementComponent.class)).move(false, true);
                ClientManager.instance.sendTCP(PacketUtils.createComponentPacket(player, MovementComponent.class));
                break;
            case Input.Keys.SPACE:
                if (((MovementComponent) player.getComponent(MovementComponent.class)).jump()) {
                    JumpPacket packet = new JumpPacket();
                    packet.id = player.getWorldID();
                    ClientManager.instance.sendTCP(packet);
                }
                break;
            case Input.Keys.NUM_1:
                Selector.instance.setSelected(0);
                break;
            case Input.Keys.NUM_2:
                Selector.instance.setSelected(1);
                break;
            case Input.Keys.NUM_3:
                Selector.instance.setSelected(2);
                break;
            case Input.Keys.NUM_4:
                Selector.instance.setSelected(3);
                break;
            case Input.Keys.NUM_5:
                Selector.instance.setSelected(4);
                break;
            case Input.Keys.NUM_6:
                Selector.instance.setSelected(5);
                break;
            case Input.Keys.NUM_7:
                Selector.instance.setSelected(6);
                break;
            case Input.Keys.NUM_8:
                Selector.instance.setSelected(7);
                break;
            case Input.Keys.NUM_9:
                Selector.instance.setSelected(8);
                break;
            case Input.Keys.E:
                InventoryManager.instance.handleClick(0);
                break;
            case Input.Keys.R:
                InventoryManager.instance.handleClick(1);
                break;

        }

        return false;

    }

    @Override
    public boolean keyUp(int keycode) {

        Entity player = GameManager.instance.player;

        switch (keycode) {

            case Input.Keys.A:
                boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
                ((MovementComponent) player.getComponent(MovementComponent.class)).move(false, moveRight);
                ClientManager.instance.sendTCP(PacketUtils.createComponentPacket(player, MovementComponent.class));
                break;
            case Input.Keys.D:
                boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
                ((MovementComponent) player.getComponent(MovementComponent.class)).move(moveLeft, false);
                ClientManager.instance.sendTCP(PacketUtils.createComponentPacket(player, MovementComponent.class));
                break;

        }

        return false;

    }

    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (InventoryManager.instance.isInventoryOpened())
            return false;
        int selected = (int) (Selector.instance.getSelected() + amountY), selectorLength = Selector.instance.getInventory()[0].length;
        Selector.instance.setSelected((selectorLength + selected) % selectorLength);
        return true;
    }

}
