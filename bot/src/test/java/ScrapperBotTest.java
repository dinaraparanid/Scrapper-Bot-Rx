import com.paranid5.core.entities.user.User;
import com.paranid5.core.entities.user.UserId;
import com.paranid5.core.entities.user.UserState;
import com.paranid5.data.link.repository.LinkRepository;
import com.paranid5.data.link.repository.LinkRepositoryInMemory;
import com.paranid5.data.link.source.github.GitHubDataSourceInMemory;
import com.paranid5.data.link.source.stackoverflow.StackOverflowDataSourceInMemory;
import com.paranid5.data.user.UserDataSource;
import com.paranid5.data.user.UserDataSourceInMemory;
import com.paranid5.data.user.state_patch.HelpStatePatch;
import com.paranid5.data.user.state_patch.ListStatePatch;
import com.paranid5.data.user.state_patch.StartStatePatch;
import com.paranid5.data.user.state_patch.TrackLinkStatePatch;
import com.paranid5.data.user.state_patch.TrackStatePatch;
import com.paranid5.data.user.state_patch.UntrackLinkStatePatch;
import com.paranid5.data.user.state_patch.UntrackStatePatch;
import com.paranid5.utils.TestUtils;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;

@SpringBootTest(
    classes = {
        UserDataSourceInMemory.class,
        GitHubDataSourceInMemory.class,
        StackOverflowDataSourceInMemory.class,
        LinkRepositoryInMemory.class
    }
)
public final class ScrapperBotTest {
    private static final User vasyan = new User(0, 0, "Васян", "Петросян");
    private static final User tolik = new User(1, 1, "Толик", "Алкоголик");
    private static final User danila = new User(2, 2, "Данила", "Из Нижнего Тагила");

    private static final String ghLink1 = "https://github.com/dinaraparanid/Crescendo";
    private static final String ghLink2 = "https://github.com/dinaraparanid";
    private static final String ghLink3 = "https://github.com/dinaraparanid/Scrapper-Bot";

    private static final String soLink1 = "https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key";
    private static final String soLink2 = "https://stackoverflow.com/questions/32580257/tablayout-set-spacing-or-margin-each-tab";
    private static final String soLink3 = "https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview";

    @Autowired
    private UserDataSource userSrc;

    @Autowired
    private LinkRepository linkRep;

    private ScrapperBotMock bot;

    private void launchCollectors() {
        bot.launchBot();

        bot.linkResponseSource()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(System.out::println);

        linkRep.launchSourceLinksMonitoring(new LinkResponseChannelMock(bot));
    }

    private void assertVals(
        @NonNull Map<UserId, UserState> requiredStates
    ) throws InterruptedException {
        Thread.sleep(300);

        userSrc
            .userStatesSource()
            .test()
            .assertValue(it ->
                TestUtils.matches(it, requiredStates)
            );
    }

    @Test
    void test1() throws InterruptedException {
        final var interactor = new BotInteractorMock(
            linkRep,
            new HelpStatePatch(userSrc),
            new ListStatePatch(userSrc),
            new StartStatePatch(userSrc),
            new TrackLinkStatePatch(userSrc),
            new TrackStatePatch(userSrc),
            new UntrackStatePatch(userSrc),
            new UntrackLinkStatePatch(userSrc)
        );

        bot = new ScrapperBotMock(userSrc, linkRep, interactor);

        launchCollectors();

        bot.sendStartMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.StartSentState(vasyan)
            )
        );

        bot.sendTrackMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackSentState(vasyan)
            )
        );

        bot.sendTextMessage(vasyan, "bebra").blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackSentState(vasyan)
            )
        );

        bot.sendTextMessage(vasyan, ghLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan)
            )
        );

        bot.sendUntrackMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.UntrackSentState(vasyan)
            )
        );

        bot.sendTextMessage(vasyan, ghLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.UntrackLinkSentState(vasyan)
            )
        );
    }

    @Test
    void test2() throws InterruptedException {
        final var interactor = new BotInteractorMock(
            linkRep,
            new HelpStatePatch(userSrc),
            new ListStatePatch(userSrc),
            new StartStatePatch(userSrc),
            new TrackLinkStatePatch(userSrc),
            new TrackStatePatch(userSrc),
            new UntrackStatePatch(userSrc),
            new UntrackLinkStatePatch(userSrc)
        );

        bot = new ScrapperBotMock(userSrc, linkRep, interactor);

        launchCollectors();

        bot.sendTrackMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackSentState(vasyan)
                )
        );

        bot.sendTextMessage(vasyan, ghLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan)
            )
        );

        bot.sendTrackMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackSentState(vasyan)
            )
        );

        bot.sendTextMessage(vasyan, ghLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan)
            )
        );

        bot.sendUntrackMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.UntrackSentState(vasyan)
            )
        );

        bot.sendTextMessage(vasyan, ghLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.UntrackLinkSentState(vasyan)
                )
        );
    }

    @Test
    void test3() throws InterruptedException {
        final var interactor = new BotInteractorMock(
            linkRep,
            new HelpStatePatch(userSrc),
            new ListStatePatch(userSrc),
            new StartStatePatch(userSrc),
            new TrackLinkStatePatch(userSrc),
            new TrackStatePatch(userSrc),
            new UntrackStatePatch(userSrc),
            new UntrackLinkStatePatch(userSrc)
        );

        bot = new ScrapperBotMock(userSrc, linkRep, interactor);

        launchCollectors();

        bot.sendStartMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.StartSentState(vasyan)
            )
        );

        bot.sendHelpMessage(tolik).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.StartSentState(vasyan),
                new UserId(tolik.id()), new UserState.HelpSentState(tolik)
            )
        );

        bot.sendListMessage(danila).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.StartSentState(vasyan),
                new UserId(tolik.id()), new UserState.HelpSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTrackMessage(vasyan).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackSentState(vasyan),
                new UserId(tolik.id()), new UserState.HelpSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTextMessage(vasyan, "bebra").blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackSentState(vasyan),
                new UserId(tolik.id()), new UserState.HelpSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTextMessage(vasyan, ghLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.HelpSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTrackMessage(tolik).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTextMessage(tolik, soLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackLinkSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTrackMessage(tolik).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTextMessage(tolik, soLink1).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackLinkSentState(tolik),
                new UserId(danila.id()), new UserState.LinkListSentState(danila)
            )
        );

        bot.sendTrackMessage(danila).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackLinkSentState(tolik),
                new UserId(danila.id()), new UserState.TrackSentState(danila)
            )
        );

        bot.sendTextMessage(danila, ghLink3).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackLinkSentState(tolik),
                new UserId(danila.id()), new UserState.TrackLinkSentState(danila)
            )
        );

        bot.sendUntrackMessage(danila).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackLinkSentState(tolik),
                new UserId(danila.id()), new UserState.UntrackSentState(danila)
            )
        );

        bot.sendTextMessage(danila, ghLink3).blockingAwait();

        assertVals(
            Map.of(
                new UserId(vasyan.id()), new UserState.TrackLinkSentState(vasyan),
                new UserId(tolik.id()), new UserState.TrackLinkSentState(tolik),
                new UserId(danila.id()), new UserState.UntrackLinkSentState(danila)
            )
        );
    }
}
