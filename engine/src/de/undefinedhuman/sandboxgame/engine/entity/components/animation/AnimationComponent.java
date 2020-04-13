package de.undefinedhuman.sandboxgame.engine.entity.components.animation;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

import java.util.Collection;
import java.util.HashMap;

public class AnimationComponent extends Component {

    private HashMap<String, Animation> animations;

    private String currentAnimation;
    private float animationTime = 0, animationTimeMultiplier = 1;

    public AnimationComponent(String defaultAnimation, HashMap<String, Animation> animations) {
        this.animations = animations;
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
        if (currentAnimation.equalsIgnoreCase(animationName)) return;
        currentAnimation = animationName;
        animationTime = 0;
    }

    public int getAnimationFrameIndex() {
        return (int) (getCurrentAnimation().bounds.getVector2().x + getLocalFrameIndex());
    }

    public Animation getCurrentAnimation() {
        return animations.get(currentAnimation);
    }

    private int getLocalFrameIndex() {

        Animation currentParam = getCurrentAnimation();

        if (currentParam.getSize() == 1) return -1;

        int frameNumber = (int) (animationTime / currentParam.frameTime.getFloat());
        switch (currentParam.playMode.getPlayMode()) {
            case NORMAL:
                frameNumber = Math.min(currentParam.getSize() - 2, frameNumber - 1);
                break;
            case LOOP:
                frameNumber = (frameNumber % currentParam.getSize()) - 1;
                break;
        }

        return frameNumber;

    }

    public Collection<Animation> getAnimations() {
        return animations.values();
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
