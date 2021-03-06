package de.undefinedhuman.projectcreate.core.ecs.visual.animation;

import de.undefinedhuman.projectcreate.engine.ecs.Component;

import java.util.HashMap;

public class AnimationComponent implements Component {

    private HashMap<String, Animation> animations;

    private String currentAnimation;
    private float animationTime = 0, animationTimeMultiplier = 1;

    public AnimationComponent(String defaultAnimation, HashMap<String, Animation> animations) {
        this.animations = animations;
        this.currentAnimation = defaultAnimation;
    }

    public void setAnimation(String animationName) {
        if (currentAnimation.equalsIgnoreCase(animationName)) return;
        currentAnimation = animationName;
        animationTime = 0;
    }

    public int getAnimationFrameIndex() {
        return (int) (getCurrentAnimation().bounds.getValue().x + getLocalFrameIndex());
    }

    public Animation getCurrentAnimation() {
        return animations.get(currentAnimation);
    }

    private int getLocalFrameIndex() {
        Animation currentAnimation = getCurrentAnimation();
        if (currentAnimation.getSize() == 1)
            return 0;
        int frameNumber = (int) (animationTime / currentAnimation.frameTime.getValue());
        switch (currentAnimation.playMode.getValue()) {
            case NORMAL:
                frameNumber = Math.min(currentAnimation.getSize() - 1, frameNumber);
                break;
            case LOOP:
                frameNumber = (frameNumber % currentAnimation.getSize());
                break;
        }

        return frameNumber;

    }

    public AnimationComponent addAnimationTime(float delta) {
        this.animationTime += delta * animationTimeMultiplier;
        this.animationTime %= 1000000;
        return this;
    }

    public void setAnimationTimeMultiplier(float animationTimeMultiplier) {
        this.animationTimeMultiplier = animationTimeMultiplier;
    }

}
