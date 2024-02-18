import com.paranid5.utils.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

public final class MapsConcatTest {
    @Test
    void test() {
        final var map1 = Map.of(
            "Bebra1",
            List.of(1, 2, 3),
            "Bebra2",
            List.of(1, 2, 3),
            "Bebra3",
            List.of(1, 2, 3)
        );

        final var map2 = Map.of(
            "Bebra1",
            List.of(4, 5, 6),
            "Bebra2",
            List.of(4, 5, 6),
            "Bebra4",
            List.of(4, 5, 6)
        );

        final var res = Maps.concat(map1, map2);

        final var match = Map.of(
            "Bebra1",
            List.of(1, 2, 3, 4, 5, 6),
            "Bebra2",
            List.of(1, 2, 3, 4, 5, 6),
            "Bebra3",
            List.of(1, 2, 3),
            "Bebra4",
            List.of(4, 5, 6)
        );

        Assertions.assertEquals(res, match);
        Assertions.assertNotSame(res, map1);
        Assertions.assertNotSame(res, map2);
        Assertions.assertNotSame(res, match);
    }
}
