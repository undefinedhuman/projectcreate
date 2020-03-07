package de.undefinedhuman.sandboxgame.entity.ecs.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class RightArmComponent extends Component {

    public float shakeAngle = 0;
    public boolean shakeDirection = false;
    public Vector2 turnedOffset, origin, shoulderPosOffset;
    private String textureName, selectedTexture;

    public RightArmComponent(Entity entity, String textureName, Vector2 turnedOffset, Vector2 origin, Vector2 shoulderPosOffset, String selectedTexture) {

        super(entity);
        this.textureName = textureName;
        this.turnedOffset = turnedOffset;
        this.origin = origin;
        this.shoulderPosOffset = shoulderPosOffset;
        this.selectedTexture = selectedTexture;
        this.type = ComponentType.RIGHTARM;

    }

    @Override
    public void receive(LineSplitter splitter) {
        this.shakeAngle = splitter.getNextFloat();
        this.shakeDirection = splitter.getNextBoolean();
    }

    @Override
    public void send(LineWriter writer) {}

    public String getSelectedTexture() { return selectedTexture; }

    public String getTextureName() { return textureName; }

}
