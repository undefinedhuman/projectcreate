package de.undefinedhuman.sandboxgame.engine.entity.components.movement;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;

public class MovementComponent extends Component {

    public Vector2 velocity = new Vector2();
    public Vector2i previousSlopeTile = new Vector2i();
    public boolean canJump = false, isJumping = false, isOnSlope = false;

    private float jumpTans, speed, jumpSpeed, gravity;
    private int direction = 0;

    public MovementComponent(float speed, float jumpSpeed, float gravity, float jumpTans) {
        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.gravity = gravity;
        this.jumpTans = jumpTans;
        this.type = ComponentType.MOVEMENT;
    }

    public float getGravity() {
        return gravity;
    }
    public float getSpeed() {
        return speed;
    }
    public float getJumpTans() {
        return jumpTans;
    }
    public float getCurrentSpeed() {
        return direction * speed;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
        if(canJump && isJumping) isJumping = false;
    }
    
    public boolean jump() {
        if (!this.canJump) return false;
        setJump();
        return true;
    }
    public void setJump() {
        this.velocity.y = jumpSpeed;
        this.canJump = false;
        this.isJumping = true;
    }
    public void move(boolean left, boolean right) {
        direction = left ? -1 : right ? 1 : 0;
    }

    @Override
    public void receive(LineSplitter splitter) {
        this.direction = splitter.getNextInt();
    }

    @Override
    public void send(LineWriter writer) {
        writer.writeInt(direction);
    }

}
