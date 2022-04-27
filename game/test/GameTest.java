import de.undefinedhuman.projectcreate.core.utils.Tools;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class CoreTest {

    @Test
    void testCore() {
        Assertions.assertThat(Tools.mix(0, 1f, 0)).isEqualTo(0f);
        Assertions.assertThat(Tools.mix(0, 1f, 0.5f)).isEqualTo(0.5f);
        Assertions.assertThat(Tools.mix(0, 1f, 1f)).isEqualTo(1f);
    }

    @Test
    void testCoreTwo() {
        Assertions.assertThat(Tools.isEqual(2, 2)).isEqualTo(1);
        Assertions.assertThat(Tools.isEqual(3, 2)).isZero();
        Assertions.assertThat(Tools.isEqual(4, 2)).isZero();
    }

}
