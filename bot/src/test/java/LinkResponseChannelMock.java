import com.paranid5.core.bot.ScrapperBot;
import com.paranid5.core.entities.link.LinkResponse;
import com.paranid5.data.link.response.LinkResponseChannel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public final class LinkResponseChannelMock implements LinkResponseChannel {
    private final ScrapperBot bot;

    public LinkResponseChannelMock(
        @NonNull ScrapperBot bot
    ) {
        this.bot = bot;
    }

    @Override
    public @NonNull Completable respondLinkStorage(@NonNull LinkResponse response) {
        return bot.acquireResponse(response);
    }
}
