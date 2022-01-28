package de.undefinedhuman.projectcreate.engine.test.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.System;
import org.junit.jupiter.api.*;

@Tag("Unit")
class SystemTest {

    @DisplayName("System Processing Utility Functions Test")
    @Test
    void testOrderingAndExecutionOfSystemProcessingFunctions() {
        TestSystem system = new TestSystem();
        system.update(0f);
        Assertions.assertTrue(system.getOrder().equalsIgnoreCase("abc"));
    }

    static class TestSystem extends System {

        private final StringBuilder order = new StringBuilder();

        @Override
        protected void start() {
            order.append("a");
        }

        @Override
        protected void process(float delta) {
            order.append("b");
        }

        @Override
        protected void end() {
            order.append("c");
        }

        private String getOrder() {
            return order.toString();
        }

    }

}
