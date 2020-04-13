package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderSystem extends System {

    public static RenderSystem instance;
    public static boolean dirty = true;

    private HashMap<Integer, ArrayList<SpriteData>> currentSpriteData;

    public RenderSystem() {
        if (instance == null) instance = this;
        currentSpriteData = new HashMap<>();
    }

    @Override
    public void init(Entity entity) {
        SpriteComponent spriteComponent;
        if((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null) return;
        for(SpriteData data : spriteComponent.getSpriteDataValue()) data.entity = entity;

    }

    @Override
    public void update(float delta, Entity entity) {}

    @Override
    public void render(SpriteBatch batch) {

        if (dirty) setTempSpriteData();

        for (Integer renderLevel : currentSpriteData.keySet())
            for (SpriteData data : currentSpriteData.get(renderLevel))
                if (data.isVisible()) {
                    updateSpriteData(data);
                    data.getSprite().draw(batch, data.getAlpha());
                }

        if (Variables.renderHitboxes) for (Entity entity : EntityManager.instance.getEntities().values()) {
            CollisionComponent collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION);
            if (collisionComponent != null)
                Tools.drawRect(batch, entity.getPosition().x + collisionComponent.getOffset().x, entity.getPosition().y + collisionComponent.getOffset().y, collisionComponent.getSize().x, collisionComponent.getSize().y, new Color(0.41568628f, 0.3529412f, 0.8039216f, 0.4f));
        }

    }

    @Override
    public void render(SpriteBatch batch, Entity entity) {

        // TODO Überarbeiten wie oben das es immer nur gerendert werden muss und nicht die komplette Liste neu erzeugt werden muss

        HashMap<Integer, ArrayList<SpriteData>> entitySprites = new HashMap<>();
        setSpriteData(entity, entitySprites);

        for (Integer renderLevel : entitySprites.keySet()) {

            for (SpriteData data : entitySprites.get(renderLevel))
                if (data.isVisible()) {
                    updateSpriteData(data);
                    data.getSprite().draw(batch, data.getAlpha());
                }
            entitySprites.get(renderLevel).clear();

        }

        entitySprites.clear();

    }

    private void setTempSpriteData() {
        for (ArrayList<SpriteData> spriteArray : currentSpriteData.values()) spriteArray.clear();
        currentSpriteData.clear();
        for (Entity entity : EntityManager.instance.getEntities().values()) setSpriteData(entity, currentSpriteData);
        dirty = false;
    }

    private void updateSpriteData(SpriteData data) {

        Sprite sprite = data.getSprite();

        sprite.setRotation(data.getRotation());
        sprite.setColor(data.getColor());

        Vector2 pos = data.entity.getPosition();
        sprite.setBounds(pos.x + data.getPositionOffset().x + data.getTurnedOffset().x, pos.y + data.getPositionOffset().y + data.getTurnedOffset().y, data.getSize().x != 0 ? data.getSize().x : data.entity.getSize().x, data.getSize().y != 0 ? data.getSize().y : data.entity.getSize().y);
        sprite.setFlip(data.getScale().x == -1, false);

    }

    private void setSpriteData(Entity entity, HashMap<Integer, ArrayList<SpriteData>> tempSpriteData) {

        if(!entity.hasComponent(ComponentType.SPRITE)) return;
        SpriteComponent spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE);

        for (SpriteData data : spriteComponent.getSpriteDataValue()) {
            int renderLevel = data.getRenderLevel();
            if (tempSpriteData.containsKey(renderLevel)) tempSpriteData.get(renderLevel).add(data);
            else {
                ArrayList<SpriteData> spriteData = new ArrayList<>();
                spriteData.add(data);
                tempSpriteData.put(renderLevel, spriteData);
            }
        }

    }

}
