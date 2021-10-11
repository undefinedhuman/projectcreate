package de.undefinedhuman.projectcreate.core.ecs.player.rightarm;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class RightArmComponent implements Component {

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
    }

    public String getSelectedTexture() { return selectedTexture; }

    public String getTextureName() { return textureName; }

}
