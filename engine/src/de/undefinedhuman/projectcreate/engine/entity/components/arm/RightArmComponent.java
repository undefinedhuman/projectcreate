package de.undefinedhuman.projectcreate.engine.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;

public class RightArmComponent extends Component {

    public float shakeAngle = 0;
    public boolean shakeDirection = false;
    public Vector2 turnedOffset, origin, shoulderPosOffset;

    private String textureName, selectedTexture;

    public RightArmComponent(String textureName, String selectedTexture, Vector2 turnedOffset, Vector2 origin, Vector2 shoulderPosOffset) {
        this.textureName = textureName;
        this.selectedTexture = selectedTexture;
        this.turnedOffset = turnedOffset;
        this.origin = origin;
        this.shoulderPosOffset = shoulderPosOffset;
        this.type = ComponentType.RIGHTARM;
    }

    public String getSelectedTexture() { return selectedTexture; }

    public String getTextureName() { return textureName; }

    @Override
    public void receive(LineSplitter splitter) {
        this.shakeAngle = splitter.getNextFloat();
        this.shakeDirection = splitter.getNextBoolean();
    }

    @Override
    public void send(LineWriter writer) {}

}
