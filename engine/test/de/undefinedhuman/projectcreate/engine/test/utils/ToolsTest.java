package de.undefinedhuman.projectcreate.engine.test.utils;

import de.undefinedhuman.projectcreate.engine.utils.Utils;
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
            softly.assertThat(Utils.isInRange(0, 0, 2)).isTrue();
            softly.assertThat(Utils.isInRange(1, 0, 2)).isTrue();
            softly.assertThat(Utils.isInRange(2, 0, 2)).isTrue();
        });
    }

    @DisplayName("Values out of range")
    @Test
    void testValuesOutOfRange() {
        assertThat(Utils.isInRange(-1, 0, 2)).isFalse();
        assertThat(Utils.isInRange(3, 0, 2)).isFalse();
    }

    @DisplayName("Clamp")
    @Test
    void testClamp() {
        assertThat(Utils.clamp(-1, 0, 2)).isZero();
        assertThat(Utils.clamp(3, 0, 2)).isEqualTo(2);
        assertThat(Utils.clamp(1, 0, 2)).isEqualTo(1);
        assertThat(Utils.clamp(0, 0, 2)).isZero();
        assertThat(Utils.clamp(2, 0, 2)).isEqualTo(2);
    }

}
