package de.undefinedhuman.projectcreate.core.ecs.mouse;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MouseComponent implements Component {

    private static final float SHAKE_ANGLE_MULTIPLIER = 10f;
    private static final float SHAKE_ANGLE_VELOCITY = 200f;

    public Vector2 mousePosition = new Vector2();
    public boolean isLeftMouseButtonDown = false, isRightMouseButtonDown = false, isTurned = false, canShake = false, shakeDirection = false;
    private float angle, shakeAngle;
    private Vector2 TEMP_POSITION = new Vector2();

    public float calculateCombinedAngle(Vector2 positionInWorldSpace, float delta) {
        calculateAngle(positionInWorldSpace);
        calculateShakeAngle(delta);
        return angle + shakeAngle;
    }

    public void calculateAngle(Vector2 positionInWorldSpace) {
        angle = TEMP_POSITION
                .set(mousePosition)
                .sub(positionInWorldSpace)
                .angleDeg() % 360 + (isTurned ? 95 : 85);
    }

    public void calculateShakeAngle(float delta) {
        if(!canShake) {
            shakeAngle = 0;
            return;
        }

        if(!isLeftMouseButtonDown && !isRightMouseButtonDown) {
            shakeAngle = shakeAngle > 0.05f ? shakeAngle - shakeAngle * SHAKE_ANGLE_MULTIPLIER * delta : 0;
            return;
        }

        angle = (shakeDirection ? 1 : -1) * SHAKE_ANGLE_VELOCITY * delta;
        shakeDirection = shakeDirection ? !(shakeAngle > 20f) : shakeAngle < -20f;
    }

    public float getAngle() {
        return angle;
    }

    public float getShakeAngle() {
        return shakeAngle;
    }
}
