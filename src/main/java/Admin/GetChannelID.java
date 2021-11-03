package Admin;

import Util.Helper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GetChannelID extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (Helper.messageReceived("getChannelID", event)){
            Helper.queueMessage(event.getChannel().getId(), event);
        }
    }
}
