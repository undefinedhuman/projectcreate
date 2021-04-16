package de.undefinedhuman.projectcreate.engine.test.utils;

import de.undefinedhuman.projectcreate.engine.utils.Tools;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("Unit")
class ToolsTest {

    @DisplayName("Values in range")
    @Test
    void testValuesInRange() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(Tools.isInRange(0, 0, 2)).isTrue();
            softly.assertThat(Tools.isInRange(1, 0, 2)).isTrue();
            softly.assertThat(Tools.isInRange(2, 0, 2)).isTrue();
        });
    }

    @DisplayName("Values out of range")
    @Test
    void testValuesOutOfRange() {
        assertThat(Tools.isInRange(-1, 0, 2)).isFalse();
        assertThat(Tools.isInRange(3, 0, 2)).isFalse();
    }

    @DisplayName("Clamp")
    @Test
    void testClamp() {
        assertThat(Tools.clamp(-1, 0, 2)).isZero();
        assertThat(Tools.clamp(3, 0, 2)).isEqualTo(2);
        assertThat(Tools.clamp(1, 0, 2)).isEqualTo(1);
        assertThat(Tools.clamp(0, 0, 2)).isZero();
        assertThat(Tools.clamp(2, 0, 2)).isEqualTo(2);
    }

}
