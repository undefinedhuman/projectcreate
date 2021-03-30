package de.undefinedhuman.projectcreate.core.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4i;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.entity.EntityManager;
import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.core.utils.Tools;
import de.undefinedhuman.projectcreate.core.world.World;

public class CameraManager extends Manager {

    public static CameraManager instance;

    public static OrthographicCamera gameCamera, guiCamera;
    public Vector4i blockBounds = new Vector4i(), chunkBounds = new Vector4i();

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
        updateCameraPosition();
        computeBounds();
    }

    private void computeBounds() {
        int viewportWidth = (int) (gameCamera.zoom * gameCamera.viewportWidth * 0.5f + Variables.BLOCK_SIZE * 2),
            viewportHeight = (int) (gameCamera.zoom * gameCamera.viewportHeight * 0.5f + Variables.BLOCK_SIZE * 2);

        this.blockBounds
                .set(gameCamera.position.x, gameCamera.position.y, gameCamera.position.x, gameCamera.position.y)
                .add(-viewportWidth, -viewportHeight, viewportWidth, viewportHeight)
                .div(Variables.BLOCK_SIZE);

        this.chunkBounds
                .set(blockBounds)
                .div(Variables.CHUNK_SIZE)
                .add(-1, -1, 1, 1)
                .setY(Math.max(chunkBounds.y, 0))
                .setW(Math.min(chunkBounds.w, EntityManager.instance.getChunkSize().y - 1));
    }

    private void updateCameraPosition() {
        if (GameManager.instance.player == null) return;
        int cameraYBounds = (int) (gameCamera.viewportHeight * gameCamera.zoom * 0.5f);
        // If lerp gets added again, make sure, that if the player gets teleported to the other side of the world the camera sets with him, otherwise there will be some kind of laggy movement
        gameCamera.position
                .set(new Vector3(GameManager.instance.player.getCenterPosition(), 0))
                .y = Tools.clamp(gameCamera.position.y, cameraYBounds, World.instance.pixelSize.y - cameraYBounds);
        gameCamera.update();
    }

}
