package de.undefinedhuman.sandboxgame.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import org.lwjgl.util.vector.Vector4f;

public class CameraManager extends Manager {

    public static CameraManager instance;

    public OrthographicCamera gameCamera, guiCamera;
    public Vector4f blockBounds = new Vector4f(), chunkBounds = new Vector4f();

    public CameraManager() {
        gameCamera = new OrthographicCamera();
        guiCamera = new OrthographicCamera();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.setToOrtho(false, width / Variables.GAME_CAMERA_ZOOM, height / Variables.GAME_CAMERA_ZOOM);
        guiCamera.setToOrtho(false, width, height);
    }

    @Override
    public void update(float delta) {
        gameCamera.update();
        computeBounds();
    }

    public void computeBounds() {

        int viewportWidth = (int) (gameCamera.zoom * gameCamera.viewportWidth * 0.5f - Variables.BLOCK_SIZE * 2) ,
            viewportHeight = (int) (gameCamera.zoom * gameCamera.viewportHeight * 0.5f - Variables.BLOCK_SIZE * 2);

        this.blockBounds.set(
                (int) ((gameCamera.position.x - viewportWidth) / Variables.BLOCK_SIZE),
                (int) ((gameCamera.position.y - viewportHeight) / Variables.BLOCK_SIZE),
                (int) ((gameCamera.position.x + viewportWidth) / Variables.BLOCK_SIZE),
                (int) ((gameCamera.position.y + viewportHeight) / Variables.BLOCK_SIZE));

    }

}
