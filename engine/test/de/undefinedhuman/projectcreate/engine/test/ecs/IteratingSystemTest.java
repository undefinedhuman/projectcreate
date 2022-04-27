package de.undefinedhuman.projectcreate.engine.test.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.Blueprint;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

@Tag("Unit")
@Tag("ECS")
@Tag("System")
public class IteratingSystemTest {

    private Entity entity1, entity2, entity3, entity4;
    private TestSystem system;

    @BeforeEach
    void setup() {
        EntityManager.getInstance().removeAllEntities();
        Blueprint blueprint = new Blueprint(0);
        entity1 = EntityManager.getInstance().createEntity(blueprint, 0);
        entity1.add(new TransformComponent(), new SpriteComponent(), new TagComponent());
        entity2 = EntityManager.getInstance().createEntity(blueprint, 1);
        entity2.add(new TransformComponent(), new SpriteComponent(), new TagComponent());
        entity3 = EntityManager.getInstance().createEntity(blueprint, 2);
        entity3.add(new TransformComponent(), new TagComponent());
        entity4 = EntityManager.getInstance().createEntity(blueprint, 3);
        entity4.add(new TransformComponent(), new SpriteComponent(), new TagComponent());
        EntityManager.getInstance().addEntity(entity1, entity2, entity3);
        this.system = new TestSystem();
        EntityManager.getInstance().addSystems(system);
        EntityManager.getInstance().update(0f);
    }

    @DisplayName("Iterating System Test")
    @Test
    void testIteratingOfEntities() {
        Assertions.assertEquals(2, system.processedEntities.size());
        Assertions.assertTrue(system.processedEntities.contains(entity1));
        Assertions.assertTrue(system.processedEntities.contains(entity2));
        Assertions.assertFalse(system.processedEntities.contains(entity3));
        Assertions.assertFalse(system.processedEntities.contains(entity4));
    }

    @DisplayName("Iterating System Component Removal Test")
    @Test
    void testIteratingWithComponentRemoval() {
        system.processedEntities.clear();
        entity2.remove(SpriteComponent.class);
        EntityManager.getInstance().update(0f);
        Assertions.assertEquals(1, system.processedEntities.size());
        Assertions.assertTrue(system.processedEntities.contains(entity1));
        Assertions.assertFalse(system.processedEntities.contains(entity2));
        Assertions.assertFalse(system.processedEntities.contains(entity3));
        Assertions.assertFalse(system.processedEntities.contains(entity4));
    }

    @DisplayName("Iterating System Component Addition Test")
    @Test
    void testIteratingWithComponentAddition() {
        system.processedEntities.clear();
        entity3.add(new SpriteComponent());
        EntityManager.getInstance().update(0f);
        Assertions.assertEquals(3, system.processedEntities.size());
        Assertions.assertTrue(system.processedEntities.contains(entity1));
        Assertions.assertTrue(system.processedEntities.contains(entity2));
        Assertions.assertTrue(system.processedEntities.contains(entity3));
        Assertions.assertFalse(system.processedEntities.contains(entity4));
    }

    @DisplayName("Iterating System Entity Addition Test")
    @Test
    void testIteratingWithEntityAddition() {
        system.processedEntities.clear();
        EntityManager.getInstance().addEntity(entity4);
        EntityManager.getInstance().update(0f);
        Assertions.assertEquals(3, system.processedEntities.size());
        Assertions.assertTrue(system.processedEntities.contains(entity1));
        Assertions.assertTrue(system.processedEntities.contains(entity2));
        Assertions.assertFalse(system.processedEntities.contains(entity3));
        Assertions.assertTrue(system.processedEntities.contains(entity4));
    }

    @DisplayName("Iterating System Entity Removal Test")
    @Test
    void testIteratingWithEntityRemoval() {
        EntityManager.getInstance().addEntity(entity4);
        EntityManager.getInstance().update(0f);
        Assertions.assertTrue(system.processedEntities.contains(entity4));
        system.processedEntities.clear();
        EntityManager.getInstance().removeEntity(entity4.getWorldID());
        EntityManager.getInstance().update(0f);
        Assertions.assertEquals(2, system.processedEntities.size());
        Assertions.assertTrue(system.processedEntities.contains(entity1));
        Assertions.assertTrue(system.processedEntities.contains(entity2));
        Assertions.assertFalse(system.processedEntities.contains(entity3));
        Assertions.assertFalse(system.processedEntities.contains(entity4));
    }

    @All({TransformComponent.class, SpriteComponent.class})
    static class TestSystem extends IteratingSystem {

        private ArrayList<Entity> processedEntities = new ArrayList<>();

        @Override
        public void processEntity(float delta, Entity entity) {
            processedEntities.add(entity);
        }

    }

    static class TransformComponent implements Component { }
    static class SpriteComponent implements Component { }
    static class TagComponent implements Component { }

}
