package de.undefinedhuman.projectcreate.game.screen;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.projectcreate.engine.utils.ScreenAdapter;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.game.world.WorldGenerator;
import de.undefinedhuman.projectcreate.game.world.settings.BiomeSetting;
import de.undefinedhuman.projectcreate.game.world.settings.WorldSetting;

import java.util.Random;

public class TestScreen extends ScreenAdapter {

    public static TestScreen instance;

    private static final Random RANDOM = new Random();

    @Override
    public void show() {

        WorldGenerator.instance = new WorldGenerator();
        WorldGenerator.instance.generateTestWorld("Main", WorldSetting.DEV, BiomeSetting.DEV);

        ClientManager.getInstance().connect();
        // ClientManager.getInstance().sendTCP(new InitPacket());

        Main.getInstance().setScreen(GameScreen.getInstance());

        Gdx.graphics.setResizable(true);

    }

}
