package de.undefinedhuman.projectcreate.game.screen;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.engine.utils.ScreenAdapter;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.world.WorldGenerator;
import de.undefinedhuman.projectcreate.game.world.settings.BiomeSetting;
import de.undefinedhuman.projectcreate.game.world.settings.WorldSetting;

public class TestScreen extends ScreenAdapter {

    public static TestScreen instance;

    @Override
    public void show() {

        WorldGenerator.instance = new WorldGenerator();
        WorldGenerator.instance.generateTestWorld("Main", WorldSetting.DEV, BiomeSetting.DEV);

        ClientManager.getInstance().connect();

        LoginPacket packet = new LoginPacket();
        packet.name = "GentleXD";
        ClientManager.getInstance().sendTCP(packet);

        Gdx.graphics.setResizable(true);

    }

}
