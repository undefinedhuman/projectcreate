package de.undefinedhuman.sandboxgame.engine.entity.components.movement;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public class MovementComponent extends Component {

    public Vector2 velocity;
    public boolean moveRight = false, moveLeft = false, beginHike = false;
    public boolean canJump = false;

    private float speed, jumpSpeed, gravity;

    public MovementComponent(float speed, float jumpSpeed, float gravity) {
        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.gravity = gravity;
        this.velocity = new Vector2(0, 0);
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

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public void forceJump() {
        this.velocity.y = (jumpSpeed * 2.3F);
        setCanJump(false);
    }

    @Override
    public void receive(LineSplitter splitter) {
        move(splitter.getNextBoolean(), splitter.getNextBoolean());
    }

    public void move(boolean left, boolean right) {
        this.moveLeft = left;
        this.moveRight = right;
    }

    @Override
    public void send(LineWriter writer) {
        writer.writeBoolean(moveLeft);
        writer.writeBoolean(moveRight);
    }

}
