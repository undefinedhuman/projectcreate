package de.undefinedhuman.sandboxgame.entity.ecs.components.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;

public class AnimationParam {

    public String[] spriteDataName;
    public Vector2 animationBounds;
    public float frameTime;
    public Animation.PlayMode mode;

    public int size;

    public AnimationParam(String[] dataTexture, Vector2 animationBounds, float frameTime, Animation.PlayMode mode) {

        this.spriteDataName = dataTexture;
        this.animationBounds = animationBounds;
        this.frameTime = frameTime;
        this.mode = mode;
        this.size = (int) (animationBounds.y - animationBounds.x + 1);

    }

    public static AnimationParam load(FileReader reader) {

        int size = reader.getNextInt();
        String[] textures = new String[size];
        for (int i = 0; i < size; i++) textures[i] = reader.getNextString();
        float frameTime = reader.getNextFloat();
        Vector2 animationBounds = reader.getNextVector2();
        return new AnimationParam(textures, animationBounds, frameTime, Animation.PlayMode.valueOf(reader.getNextString()));

    }

}
