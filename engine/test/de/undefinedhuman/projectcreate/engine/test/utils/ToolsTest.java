package de.undefinedhuman.projectcreate.engine.test.utils;

import de.undefinedhuman.projectcreate.engine.utils.Tools;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolsTest {

    @DisplayName("Values in range test")
    @Test
    void testValuesInRange() {
        assertTrue(Tools.isInRange(0, 0, 2));
        assertTrue(Tools.isInRange(1, 0, 2));
        assertTrue(Tools.isInRange(2, 0, 2));
    }

    @DisplayName("Is In Range test")
    @Test
    void testValuesOutOfRange() {
        assertFalse(Tools.isInRange(-1, 0, 2));
        assertFalse(Tools.isInRange(3, 0, 2));
    }

}
