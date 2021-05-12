package de.undefinedhuman.projectcreate.game.background.clouds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.game.background.BackgroundObject;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class Cloud extends BackgroundObject {

    public float alpha;
    private TextureRegion texture;

    public Cloud(String texture, Vector2 startPos, int layerID) {
        super(Tools.getTextureSize(TextureManager.instance.getTexture(texture)), startPos, -(Tools.random.nextFloat() * 7f + 1), layerID);
        this.texture = TextureManager.instance.getTexture(texture);
        this.alpha = 1f - layerID * 0.2f;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        Color batchColor = batch.getColor();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(texture, (camera.position.x - camera.viewportWidth * 0.5f) + position.x, position.y, size.x, size.y);
        batch.setColor(batchColor);
    }

}
