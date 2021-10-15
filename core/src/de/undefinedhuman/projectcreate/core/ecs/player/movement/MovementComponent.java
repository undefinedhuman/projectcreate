package de.undefinedhuman.projectcreate.core.ecs.player.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

import java.util.LinkedList;

public class MovementComponent implements Component, NetworkSerializable {

    public Vector2 velocity = new Vector2();

    private boolean canJump = false;
    private float jumpTans, speed, jumpSpeed, gravity;
    private int direction = 0;

    public long latestPositionPacketTime = 0;
    public long lastPositionPacketTimeLocal = 0;
    public Vector2 predictedPosition = new Vector2();
    public float historyLength = 0f, delta = 0f;
    public LinkedList<MovementFrame> movementHistory = new LinkedList<>();

    public MovementComponent(float speed, float jumpSpeed, float gravity, float jumpTans) {
        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.gravity = gravity;
        this.jumpTans = jumpTans;
    }

    public float getGravity() {
        return gravity;
    }
    public float getSpeed() {
        return speed;
    }
    public int getDirection() {
        return direction;
    }
    public float getJumpTans() {
        return jumpTans;
    }
    
    public boolean jump() {
        if (!this.canJump) return false;
        setJump();
        return true;
    }

    public void forceJump() {
        setJump();
    }

    public void setJump() {
        this.velocity.y = jumpSpeed;
        this.canJump = false;
    }

    public void setCanJump() {
        this.canJump = true;
    }

    public void move(boolean left, boolean right) {
        direction = left ? -1 : right ? 1 : 0;
    }

    public void move(int direction) {
        this.direction = direction;
    }

    @Override
    public void parse(LineSplitter splitter) {
        this.direction = splitter.getNextInt();
    }

    @Override
    public void serialize(LineWriter writer) {
        writer.writeInt(direction);
    }

    public static class MovementFrame {
        public Vector2 position;
        public Vector2 velocity;
        public float delta;
    }

}
