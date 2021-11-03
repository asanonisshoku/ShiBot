package Currency.Events.CacheEventMisc;

import Util.Helper;
import Util.MongoHandler;
import Util.BotUser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class LastClaimEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (Helper.messageReceived("nextClaimEvent", event)){
            BotUser botUser = MongoHandler.getUser(Helper.getUserID(event));
            if (botUser.isAdmin()){
                Helper.queueMessage("The next claim event is in: " + CacheEventController.getLastEventTime() + " minutes", event);
            }
        }
    }
}
