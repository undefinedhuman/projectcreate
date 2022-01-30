package de.undefinedhuman.projectcreate.engine.test.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class EntityTest {

    private final ComponentMapper<TransformComponent> tm = ComponentMapper.create(TransformComponent.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.create(SpriteComponent.class);

    @DisplayName("Entity Initialization")
    @Test
    public void noComponents () {
        Blueprint blueprint = new Blueprint(0);
        Entity entity = EntityManager.getInstance().createEntity(blueprint, 0);
        Assertions.assertEquals(0, entity.getComponents().size());
        Assertions.assertTrue(entity.getComponentBits().isEmpty());
        Assertions.assertNull(tm.get(entity));
        Assertions.assertNull(sm.get(entity));
        Assertions.assertFalse(tm.has(entity));
        Assertions.assertFalse(sm.has(entity));
    }

    @DisplayName("Entity Component Addition")
    @Test
    void entityInitialization() {
        TransformComponent transformComponent = new TransformComponent();
        SpriteComponent spriteComponent = new SpriteComponent();
        Blueprint blueprint = new Blueprint(0);
        Entity entity = EntityManager.getInstance().createEntity(blueprint, 0);
        entity.add(transformComponent, spriteComponent);
        Assertions.assertEquals(2, entity.getComponents().size());
        Assertions.assertEquals(transformComponent, entity.getComponent(TransformComponent.class));
        Assertions.assertEquals(spriteComponent, entity.getComponent(SpriteComponent.class));
    }

    @DisplayName("Entity Component Removal")
    @Test
    void removeComponentsFromEntity() {
        Blueprint blueprint = new Blueprint(0);
        Entity entity = EntityManager.getInstance().createEntity(blueprint, 0);
        entity.add(new SpriteComponent());
        entity.remove(SpriteComponent.class);
        Assertions.assertNull(entity.getComponent(SpriteComponent.class));
        Assertions.assertNull(sm.get(entity));
        Assertions.assertFalse(sm.has(entity));
    }

    @DisplayName("Entity Component Mapper")
    @Test
    void componentRetrievalViaMappers() {
        TransformComponent transformComponent = new TransformComponent();
        SpriteComponent spriteComponent = new SpriteComponent();
        Blueprint blueprint = new Blueprint(0);
        Entity entity = EntityManager.getInstance().createEntity(blueprint, 0);
        entity.add(transformComponent, spriteComponent);
        Assertions.assertTrue(tm.has(entity));
        Assertions.assertTrue(sm.has(entity));
        Assertions.assertEquals(transformComponent, tm.get(entity));
        Assertions.assertEquals(spriteComponent, sm.get(entity));
    }

    static class TransformComponent implements Component {}
    static class SpriteComponent implements Component {}

}
