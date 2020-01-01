package de.undefinedhuman.sandboxgame.entity.ecs.components.movement;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class MovementComponent extends Component {

    private float speed, jumpSpeed, gravity;
    public Vector2 velocity;

    public boolean moveRight = false, moveLeft = false, beginHike = false;
    public boolean canJump = false;

    public MovementComponent(Entity entity, float speed, float jumpSpeed, float gravity) {

        super(entity);
        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.gravity = gravity;
        this.velocity = new Vector2(0,0);
        this.type = ComponentType.MOVEMENT;

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public boolean jump() {

        if (!this.canJump) return false;

        this.velocity.y = (jumpSpeed * 2.3F);
        setCanJump(false);
        return true;

    }

    public void forceJump() {

        this.velocity.y = (jumpSpeed * 2.3F);
        setCanJump(false);

    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public void move(boolean left, boolean right) {

        this.moveLeft = left;
        this.moveRight = right;

        //this.animationTime = (right || left) ? (canJump ? 0 : animationTime) : animationTime;

    }

    @Override
    public void setNetworkData(LineSplitter s) {
        move(s.getNextBoolean(), s.getNextBoolean());
    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeBoolean(moveLeft);
        w.writeBoolean(moveRight);
    }

}
