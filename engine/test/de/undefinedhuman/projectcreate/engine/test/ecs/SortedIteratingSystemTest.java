package de.undefinedhuman.projectcreate.engine.test.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.*;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.Optional;
import de.undefinedhuman.projectcreate.engine.ecs.systems.SortedIteratingSystem;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Comparator;

@Tag("Unit")
@Tag("ECS")
@Tag("System")
public class SortedIteratingSystemTest {

    private Entity entity1, entity2, entity3;
    private TestSystem system;
    private ComponentMapper<TypeComponent> tm = ComponentMapper.create(TypeComponent.class);

    @BeforeEach
    void setup() {
        EntityManager.getInstance().removeAllEntities();
        Blueprint blueprint = new Blueprint(0);
        entity1 = EntityManager.getInstance().createEntity(blueprint, 0);
        entity1.add(new SpriteComponent(), new TypeComponent(TypeComponent.Type.FOREGROUND));
        entity2 = EntityManager.getInstance().createEntity(blueprint, 1);
        entity2.add(new SpriteComponent());
        entity3 = EntityManager.getInstance().createEntity(blueprint, 2);
        entity3.add(new SpriteComponent(), new TypeComponent(TypeComponent.Type.BACKGROUND));
        EntityManager.getInstance().addEntity(entity1, entity2, entity3);
        system = new TestSystem();
        EntityManager.getInstance().addSystems(system);
        EntityManager.getInstance().update(0f);
    }

    @DisplayName("Sorted Iterating System Test")
    @Test
    void testSortedIteratingOfEntities() {
        Assertions.assertEquals(3, system.processedEntities.size());
        Assertions.assertEquals(entity2, system.processedEntities.get(0));
        Assertions.assertEquals(entity3, system.processedEntities.get(1));
        Assertions.assertEquals(entity1, system.processedEntities.get(2));
    }

    @DisplayName("Sorted Iterating System Component Addition Test")
    @Test
    void testSortedIteratingOfEntitiesWithComponentAddition() {
        entity2.add(new TypeComponent(TypeComponent.Type.MIDDLE));
        system.processedEntities.clear();
        EntityManager.getInstance().update(0f);
        Assertions.assertTrue(tm.has(entity2));
        Assertions.assertEquals(3, system.processedEntities.size());
        Assertions.assertEquals(entity3, system.processedEntities.get(0));
        Assertions.assertEquals(entity2, system.processedEntities.get(1));
        Assertions.assertEquals(entity1, system.processedEntities.get(2));
    }

    @DisplayName("Sorted Iterating System Component Removal Test")
    @Test
    void testSortedIteratingOfEntitiesWithComponentRemoval() {
        entity3.remove(TypeComponent.class);
        system.processedEntities.clear();
        EntityManager.getInstance().update(0f);
        Assertions.assertFalse(tm.has(entity3));
        Assertions.assertEquals(3, system.processedEntities.size());
        Assertions.assertEquals(entity3, system.processedEntities.get(0));
        Assertions.assertEquals(entity2, system.processedEntities.get(1));
        Assertions.assertEquals(entity1, system.processedEntities.get(2));
    }

    @DisplayName("Sorted Iterating System Entity Removal Test")
    @Test
    void testSortedIteratingOfEntitiesWithEntityRemoval() {
        EntityManager.getInstance().removeEntity(entity1.getWorldID());
        system.processedEntities.clear();
        EntityManager.getInstance().update(0f);
        Assertions.assertEquals(2, system.processedEntities.size());
        Assertions.assertEquals(entity2, system.processedEntities.get(0));
        Assertions.assertEquals(entity3, system.processedEntities.get(1));
    }

    @DisplayName("Sorted Iterating System Entity Addition Test")
    @Test
    void testSortedIteratingOfEntitiesWithEntityAddition() {
        EntityManager.getInstance().removeEntity(entity1.getWorldID());
        system.processedEntities.clear();
        EntityManager.getInstance().addEntity(entity1);
        EntityManager.getInstance().update(0f);
        Assertions.assertEquals(3, system.processedEntities.size());
        Assertions.assertEquals(entity2, system.processedEntities.get(0));
        Assertions.assertEquals(entity3, system.processedEntities.get(1));
        Assertions.assertEquals(entity1, system.processedEntities.get(2));
    }

    @All(SpriteComponent.class)
    @Optional(TypeComponent.class)
    static class TestSystem extends SortedIteratingSystem {

        private ArrayList<Entity> processedEntities = new ArrayList<>();

        public TestSystem() {
            super(new TypeComparator());
        }

        @Override
        protected void processEntity(float delta, Entity entity) {
            processedEntities.add(entity);
        }

        static class TypeComparator implements Comparator<Entity> {

            private ComponentMapper<TypeComponent> tm = ComponentMapper.create(TypeComponent.class);

            @Override
            public int compare(Entity o1, Entity o2) {
                TypeComponent entity1TypeComponent = tm.get(o1);
                TypeComponent entity2TypeComponent = tm.get(o2);
                if(entity1TypeComponent == null) return -1;
                if(entity2TypeComponent == null) return 1;
                return Integer.compare(entity1TypeComponent.getType().ordinal(), entity2TypeComponent.getType().ordinal());
            }

        }
    }

    static class SpriteComponent implements Component { }
    static class TypeComponent implements Component {
        private Type type;

        public TypeComponent(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        enum Type {
            BACKGROUND,
            MIDDLE,
            FOREGROUND
        }
    }

}
