import com.paranid5.core.entities.link.parsers.Parsers;
import com.paranid5.core.entities.link.types.GitHubLinkType;
import com.paranid5.core.entities.link.types.StackOverflowLinkType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class LinkParserTest {
    private static final String GH_LINK = "https://github.com/dinaraparanid/Crescendo";
    private static final String SO_LINK = "https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key";

    @Test
    void test() {
        final var parsers = new Parsers();
        Assertions.assertInstanceOf(GitHubLinkType.class, parsers.parseLink(GH_LINK).get());
        Assertions.assertInstanceOf(StackOverflowLinkType.class, parsers.parseLink(SO_LINK).get());
        Assertions.assertTrue(parsers.parseLink("https://bebra.com").isEmpty());
    }
}
