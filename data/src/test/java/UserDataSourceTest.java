import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserState;
import com.paranid5.data.user.UserDataSource;
import com.paranid5.data.user.UserDataSourceInMemory;
import com.paranid5.utils.TestUtils;
import io.reactivex.rxjava3.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {UserDataSourceInMemory.class})
public final class UserDataSourceTest {
    private static final User VASYAN = new User(0, 0, "Васян", "Петросян");
    private static final User TOLIK = new User(1, 1, "Толик", "Алкоголик");
    private static final User DANILA = new User(2, 2, "Данила", "Из Нижнего Тагила");

    @Autowired
    private UserDataSource userSrc;

    private void assertVals(
        @NonNull Collection<User> requiredUsers,
        @NonNull Map<UserId, UserState> requiredStates
    ) {
        userSrc
            .usersSource()
            .test()
            .assertValue(it -> TestUtils.matches(it, requiredUsers));

        userSrc
            .userStatesSource()
            .test()
            .assertValue(it -> TestUtils.matches(it, requiredStates));
    }

    @Test
    void test() {
        userSrc
            .patchUser(VASYAN)
            .blockingAwait();

        assertVals(
            List.of(VASYAN),
            Map.of(new UserId(VASYAN.id()), new UserState.NoneState(VASYAN))
        );

        userSrc
            .patchUserState(new UserState.HelpSentState(TOLIK))
            .blockingAwait();

        assertVals(
            List.of(VASYAN, TOLIK),
            Map.of(
                new UserId(VASYAN.id()), new UserState.NoneState(VASYAN),
                new UserId(TOLIK.id()), new UserState.HelpSentState(TOLIK)
            )
        );

        userSrc
            .patchUser(DANILA)
            .blockingAwait();

        assertVals(
            List.of(VASYAN, TOLIK, DANILA),
            Map.of(
                new UserId(VASYAN.id()), new UserState.NoneState(VASYAN),
                new UserId(TOLIK.id()), new UserState.HelpSentState(TOLIK),
                new UserId(DANILA.id()), new UserState.NoneState(DANILA)
            )
        );

        userSrc
            .patchUserState(new UserState.TrackSentState(DANILA))
            .blockingAwait();

        assertVals(
            List.of(VASYAN, TOLIK, DANILA),
            Map.of(
                new UserId(VASYAN.id()), new UserState.NoneState(VASYAN),
                new UserId(TOLIK.id()), new UserState.HelpSentState(TOLIK),
                new UserId(DANILA.id()), new UserState.TrackSentState(DANILA)
            )
        );
    }
}
