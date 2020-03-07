package de.undefinedhuman.sandboxgame.entity.ecs.components.animation;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.Collection;
import java.util.HashMap;

public class AnimationComponent extends Component {

    private HashMap<String, AnimationParam> animationParams;

    private String currentAnimation;
    private float animationTime = 0, animationTimeMultiplier = 1;

    public AnimationComponent(Entity entity, String defaultAnimation, HashMap<String, AnimationParam> animationParams) {
        super(entity);
        this.animationParams = animationParams;
        this.currentAnimation = defaultAnimation;
        this.type = ComponentType.ANIMATION;
    }

    @Override
    public void init() {}

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

    public void setAnimation(String animationName) {

        if (!currentAnimation.equalsIgnoreCase(animationName)) {
            currentAnimation = animationName;
            animationTime = 0;
        }

    }

    public int getCurrentAnimationFrameID() {
        return (int) (getCurrentAnimationParam().animationBounds.x + getCurrentAnimationIndex());
    }

    public AnimationParam getCurrentAnimationParam() {
        return animationParams.get(currentAnimation);
    }

    public int getCurrentAnimationIndex() {

        AnimationParam currentParam = getCurrentAnimationParam();

        if (currentParam.size == 1) return -1;

        int frameNumber = (int) (animationTime / currentParam.frameTime);
        switch (currentParam.mode) {
            case NORMAL:
                frameNumber = Math.min(currentParam.size - 2, frameNumber - 1);
                break;
            case LOOP:
                frameNumber = (frameNumber % currentParam.size) - 1;
                break;
        }

        return frameNumber;

    }

    public Collection<AnimationParam> getAnimationParams() {
        return animationParams.values();
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public void addAnimationTime(float delta) {
        this.animationTime += delta * animationTimeMultiplier;
    }

    public void setAnimationTimeMultiplier(float animationTimeMultiplier) {
        this.animationTimeMultiplier = animationTimeMultiplier;
    }

}
