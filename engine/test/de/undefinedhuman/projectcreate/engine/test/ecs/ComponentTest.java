package de.undefinedhuman.projectcreate.engine.test.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.ComponentMapper;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.test.ecs.components.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.test.ecs.components.TransformComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class ComponentTest {

    private final ComponentMapper<TransformComponent> tm = ComponentMapper.create(TransformComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.create(SpriteComponent.class);

    @DisplayName("Entity Initialization")
    @Test
    void entityInitialization() {
        TransformComponent transformComponent = new TransformComponent();
        SpriteComponent spriteComponent = new SpriteComponent();
        Entity entity = new Entity(0, 0);
        entity.add(transformComponent);
        entity.add(spriteComponent);
        Assertions.assertEquals(2, entity.getComponents().size());
        Assertions.assertEquals(transformComponent, entity.getComponent(TransformComponent.class));
        Assertions.assertEquals(spriteComponent, entity.getComponent(SpriteComponent.class));
        Assertions.assertEquals(transformComponent, tm.get(entity));
        Assertions.assertEquals(spriteComponent, sm.get(entity));
    }

    @DisplayName("Entity Component Addition")
    @Test
    void addComponentsToEntity() {
        TransformComponent transformComponent = new TransformComponent();
        SpriteComponent spriteComponent = new SpriteComponent();
        Entity entity = new Entity(0, 0);
        entity.add(transformComponent);
        entity.add(spriteComponent);
        Assertions.assertEquals(2, entity.getComponents().size());
        Assertions.assertEquals(transformComponent, entity.getComponent(TransformComponent.class));
        Assertions.assertEquals(spriteComponent, entity.getComponent(SpriteComponent.class));
    }

    @DisplayName("Entity Component Removal")
    @Test
    void removeComponentsFromEntity() {
        TransformComponent transformComponent = new TransformComponent();
        SpriteComponent spriteComponent = new SpriteComponent();
        Entity entity = new Entity(0, 0);
        entity.add(transformComponent);
        entity.add(spriteComponent);
        Assertions.assertEquals(2, entity.getComponents().size());
        Assertions.assertEquals(transformComponent, entity.getComponent(TransformComponent.class));
        Assertions.assertEquals(spriteComponent, entity.getComponent(SpriteComponent.class));
    }

}
