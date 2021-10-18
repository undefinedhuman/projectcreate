package de.undefinedhuman.projectcreate.core.ecs.player.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

import java.util.LinkedList;

public class MovementComponent implements Component, NetworkSerializable {

    public Vector2 velocity = new Vector2();

    private boolean canJump = false;
    private float jumpTans, speed, jumpSpeed, gravity;
    private int direction = 0;

    private String idleAnimation = "", runAnimation = "", jumpAnimation = "", transitionAnimation = "", fallAnimation = "";

    public long latestPositionPacketTime = 0;
    public long lastPositionPacketTimeLocal = 0;
    public Vector2 predictedPosition = new Vector2();
    public float historyLength = 0f, delta = 0f;
    public LinkedList<MovementFrame> movementHistory = new LinkedList<>();

    public MovementComponent(float speed, float jumpSpeed, float gravity, float jumpTans, String idleAnimation, String runAnimation, String jumpAnimation, String transitionAnimation, String fallAnimation) {
        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.gravity = gravity;
        this.jumpTans = jumpTans;
        this.idleAnimation = idleAnimation;
        this.runAnimation = runAnimation;
        this.jumpAnimation = jumpAnimation;
        this.transitionAnimation = transitionAnimation;
        this.fallAnimation = fallAnimation;
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

    public String getIdleAnimation() {
        return idleAnimation;
    }

    public String getRunAnimation() {
        return runAnimation;
    }

    public String getJumpAnimation() {
        return jumpAnimation;
    }

    public String getTransitionAnimation() {
        return transitionAnimation;
    }

    public String getFallAnimation() {
        return fallAnimation;
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
        public Vector2 position = new Vector2();
        public Vector2 velocity = new Vector2();
        public float delta;

        public MovementFrame(float x, float y, float velX, float velY, float delta) {
            this.position.set(x, y);
            this.velocity.set(velX, velY);
            this.delta = delta;
        }

        public MovementFrame(PositionPacket packet, float delta) {
            this.position.set(packet.x, packet.y);
            this.velocity.set(packet.velX, packet.velY);
            this.delta = delta;
        }
    }

}
