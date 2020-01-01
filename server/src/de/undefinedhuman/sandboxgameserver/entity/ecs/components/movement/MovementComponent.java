package de.undefinedhuman.sandboxgameserver.entity.ecs.components.movement;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class MovementComponent extends Component {

    public Vector2 velocity;
    public boolean moveRight = false, moveLeft = false, canJump = false;
    private float speed, jumpSpeed, gravity;

    public MovementComponent(Entity entity, float speed, float jumpSpeed, float gravity) {

        super(entity);

        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.gravity = gravity;

        this.velocity = new Vector2(0, 0);

        this.type = ComponentType.MOVEMENT;

    }

    @Override
    public void load(FileReader reader) {}

    @Override
    public void save(FileWriter writer) {}

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeBoolean(moveLeft);
        w.writeBoolean(moveRight);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        this.moveLeft = s.getNextBoolean();
        this.moveRight = s.getNextBoolean();
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

    public void jump() {

        if (this.canJump) {

            this.velocity.y = (jumpSpeed * 2.3F);
            this.canJump = false;

        }

    }

    public void forceJump() {

        this.velocity.y = (jumpSpeed * 2.3F);
        canJump = false;

    }

    public void move(boolean left, boolean right) {

        this.moveLeft = left;
        this.moveRight = right;

    }

}
