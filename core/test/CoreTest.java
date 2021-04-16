import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class CoreTest {

    @Test
    void testCore() {
        Assertions.assertThat(true).isTrue();
    }

}
