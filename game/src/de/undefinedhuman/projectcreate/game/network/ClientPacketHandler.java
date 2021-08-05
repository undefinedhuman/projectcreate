package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.name.NameComponent;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;

public class ClientPacketHandler implements PacketHandler {
    @Override
    public void handle(LoginPacket packet) {
        Entity player = BlueprintManager.getInstance().getBlueprint(BlueprintManager.PLAYER_BLUEPRINT_ID).createInstance();
        NameComponent nameComponent = Mappers.NAME.get(player);
        if (nameComponent != null) nameComponent.setName(packet.name);
        EntityManager.getInstance().getEngine().addEntity(player);
        GameManager.getInstance().player = player;
        Gdx.app.postRunnable(() -> Main.instance.setScreen(GameScreen.getInstance()));
    }
}
