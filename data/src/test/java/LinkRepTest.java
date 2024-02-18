import com.paranid5.core.entities.user.UserId;
import com.paranid5.data.link.repository.LinkRepository;
import com.paranid5.data.link.repository.LinkRepositoryInMemory;
import com.paranid5.data.link.response.LinkResponseChannel;
import com.paranid5.data.link.response.LinkResponseChannelMock;
import com.paranid5.data.link.source.github.GitHubDataSource;
import com.paranid5.data.link.source.github.GitHubDataSourceInMemory;
import com.paranid5.data.link.source.stackoverflow.StackOverflowDataSource;
import com.paranid5.data.link.source.stackoverflow.StackOverflowDataSourceInMemory;
import com.paranid5.utils.Maps;
import com.paranid5.utils.TestUtils;
import io.reactivex.rxjava3.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(
    classes = {
        GitHubDataSourceInMemory.class,
        StackOverflowDataSourceInMemory.class
    }
)
public final class LinkRepTest {
    private static final long vasyanId = 0L;
    private static final long tolikId = 1L;
    private static final long danilaId = 2L;

    private static final String ghLink1 = "https://github.com/dinaraparanid/Crescendo";
    private static final String ghLink2 = "https://github.com/dinaraparanid";
    private static final String ghLink3 = "https://github.com/dinaraparanid/Scrapper-Bot";

    private static final String soLink1 = "https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key";
    private static final String soLink2 = "https://stackoverflow.com/questions/32580257/tablayout-set-spacing-or-margin-each-tab";
    private static final String soLink3 = "https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview";

    @Autowired
    private GitHubDataSource githubSrc;

    @Autowired
    private StackOverflowDataSource stackSrc;

    private final LinkResponseChannel channel =
        new LinkResponseChannelMock();

    private LinkRepository repository;

    private void launchCollectors() {
        repository = new LinkRepositoryInMemory(githubSrc, stackSrc);

        githubSrc.launchLinksStoreMonitoring(channel);
        githubSrc.launchLinksRemoveMonitoring(channel);

        stackSrc.launchLinksStoreMonitoring(channel);
        stackSrc.launchLinksRemoveMonitoring(channel);
    }

    private void assertVals(
        @NonNull Map<UserId, List<String>> ghMap,
        @NonNull Map<UserId, List<String>> soMap,
        @NonNull Map<UserId, List<String>> repMap
    ) throws InterruptedException {
        Thread.sleep(300);

        githubSrc
            .usersTrackingsSource()
            .test()
            .assertValue(it -> TestUtils.matches(it, ghMap));

        stackSrc
            .usersTrackingsSource()
            .test()
            .assertValue(it -> TestUtils.matches(it, soMap));

        repository
            .usersTrackingsSource()
            .test()
            .assertValue(it -> TestUtils.matches(it, repMap));
    }

    @Test
    void test() throws InterruptedException {
        launchCollectors();

        // ------------- Track -------------

        githubSrc.trackLink(vasyanId, ghLink1).blockingAwait();
        var ghMap = Map.of(new UserId(vasyanId), List.of(ghLink1));
        Map<UserId, List<String>> soMap = new HashMap<>();
        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        githubSrc.trackLink(tolikId, ghLink2).blockingAwait();

        ghMap = Map.of(
            new UserId(vasyanId), List.of(ghLink1),
            new UserId(tolikId), List.of(ghLink2)
        );

        assertVals(ghMap, soMap,Maps.concat(ghMap, soMap));

        githubSrc.trackLink(danilaId, ghLink1).blockingAwait();
        Thread.sleep(300);
        githubSrc.trackLink(danilaId, ghLink2).blockingAwait();
        Thread.sleep(300);
        githubSrc.trackLink(danilaId, ghLink3).blockingAwait();

        ghMap = Map.of(
            new UserId(vasyanId), List.of(ghLink1),
            new UserId(tolikId), List.of(ghLink2),
            new UserId(danilaId), List.of(ghLink1, ghLink2, ghLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        stackSrc.trackLink(vasyanId, soLink1).blockingAwait();
        soMap = Map.of(new UserId(vasyanId), List.of(soLink1));
        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        stackSrc.trackLink(tolikId, soLink2).blockingAwait();

        soMap = Map.of(
            new UserId(vasyanId), List.of(soLink1),
            new UserId(tolikId), List.of(soLink2)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        stackSrc.trackLink(danilaId, soLink1).blockingAwait();
        Thread.sleep(300);
        stackSrc.trackLink(danilaId, soLink2).blockingAwait();
        Thread.sleep(300);
        stackSrc.trackLink(danilaId, soLink3).blockingAwait();

        soMap = Map.of(
            new UserId(vasyanId), List.of(soLink1),
            new UserId(tolikId), List.of(soLink2),
            new UserId(danilaId), List.of(soLink1, soLink2, soLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        // ------------- Untrack -------------

        githubSrc.untrackLink(vasyanId, ghLink1).blockingAwait();

        ghMap = Map.of(
            new UserId(vasyanId), List.of(),
            new UserId(tolikId), List.of(ghLink2),
            new UserId(danilaId), List.of(ghLink1, ghLink2, ghLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        githubSrc.untrackLink(tolikId, ghLink2).blockingAwait();

        ghMap = Map.of(
            new UserId(vasyanId), List.of(),
            new UserId(tolikId), List.of(),
            new UserId(danilaId), List.of(ghLink1, ghLink2, ghLink3)
        );

        assertVals(ghMap, soMap,Maps.concat(ghMap, soMap));

        githubSrc.untrackLink(danilaId, ghLink1).blockingAwait();
        Thread.sleep(300);
        githubSrc.untrackLink(danilaId, ghLink2).blockingAwait();

        ghMap = Map.of(
            new UserId(vasyanId), List.of(),
            new UserId(tolikId), List.of(),
            new UserId(danilaId), List.of(ghLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        stackSrc.untrackLink(vasyanId, soLink1).blockingAwait();

        soMap = Map.of(
            new UserId(vasyanId), List.of(),
            new UserId(tolikId), List.of(soLink2),
            new UserId(danilaId), List.of(soLink1, soLink2, soLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        stackSrc.untrackLink(tolikId, soLink2).blockingAwait();

        soMap = Map.of(
            new UserId(vasyanId), List.of(),
            new UserId(tolikId), List.of(),
            new UserId(danilaId), List.of(soLink1, soLink2, soLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));

        stackSrc.untrackLink(danilaId, soLink1).blockingAwait();
        Thread.sleep(300);
        stackSrc.untrackLink(danilaId, soLink2).blockingAwait();

        soMap = Map.of(
            new UserId(vasyanId), List.of(),
            new UserId(tolikId), List.of(),
            new UserId(danilaId), List.of(soLink3)
        );

        assertVals(ghMap, soMap, Maps.concat(ghMap, soMap));
    }
}
