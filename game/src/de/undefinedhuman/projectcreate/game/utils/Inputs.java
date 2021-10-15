package de.undefinedhuman.projectcreate.game.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

public class Inputs extends Manager implements InputProcessor {

    private static volatile Inputs instance;

    private static final int NUM_KEY_INDEX = 7;

    private Inputs() { }

    @Override
    public boolean keyDown(int keycode) {

        Entity player = GameManager.getInstance().player;

        switch (keycode) {

            case Input.Keys.A:
                Mappers.MOVEMENT.get(player).move(true, false);
                ClientManager.getInstance().sendTCP(MovementPacket.serialize(player, Mappers.MOVEMENT.get(player)));
                break;
            case Input.Keys.D:
                Mappers.MOVEMENT.get(player).move(false, true);
                ClientManager.getInstance().sendTCP(MovementPacket.serialize(player, Mappers.MOVEMENT.get(player)));
                break;
            case Input.Keys.SPACE:
                if (!Mappers.MOVEMENT.get(player).jump())
                    break;
                ClientManager.getInstance().sendTCP(JumpPacket.serialize(player));
                break;
            case Input.Keys.NUM_0:
            case Input.Keys.NUM_1:
            case Input.Keys.NUM_2:
            case Input.Keys.NUM_3:
            case Input.Keys.NUM_4:
            case Input.Keys.NUM_5:
            case Input.Keys.NUM_6:
            case Input.Keys.NUM_7:
            case Input.Keys.NUM_8:
            case Input.Keys.NUM_9:
                Selector.getInstance().setSelected(keycode-NUM_KEY_INDEX);
                break;
            case Input.Keys.E:
                InventoryManager.getInstance().handleClick(0);
                break;
            case Input.Keys.R:
                InventoryManager.getInstance().handleClick(1);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        Entity player = GameManager.getInstance().player;

        switch (keycode) {

            case Input.Keys.A:
                boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
                player.getComponent(MovementComponent.class).move(false, moveRight);
                ClientManager.getInstance().sendTCP(MovementPacket.serialize(player, Mappers.MOVEMENT.get(player)));
                break;
            case Input.Keys.D:
                boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
                player.getComponent(MovementComponent.class).move(moveLeft, false);
                ClientManager.getInstance().sendTCP(MovementPacket.serialize(player, Mappers.MOVEMENT.get(player)));
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
        if (InventoryManager.getInstance().isInventoryOpened())
            return false;
        int selected = (int) (Selector.getInstance().getCol() + Selector.getInstance().getSelectedIndex() + amountY) % Selector.getInstance().getCol();
        Selector.getInstance().setSelected(selected);
        ClientManager.getInstance().sendTCP(SelectorPacket.serialize(Selector.getInstance().getName(), selected));
        return true;
    }

    public static Inputs getInstance() {
        if(instance != null)
            return instance;
        synchronized (Inputs.class) {
            if (instance == null)
                instance = new Inputs();
        }
        return instance;
    }

}
