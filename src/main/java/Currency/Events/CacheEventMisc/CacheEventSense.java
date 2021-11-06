package Currency.Events.CacheEventMisc;

import Util.Helper;
import Util.MongoHandler;
import Util.BotUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CacheEventSense extends Command {

    public CacheEventSense() {
        this.name = "sense";
        this.aliases = new String[]{"sense", "tingling"};
        this.help = "informs an admin when the next claim event will happen";
    }

    // A command to allow an admin to tell when the next claim event will spawn in their chosen channel.
    @Override
    protected void execute(CommandEvent commandEvent) {
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(commandEvent));

        //Failsafe if a so an non-admin'ed user can't use command at all
        if (Helper.hasBotPerms(commandEvent, botUser)){
            Helper.respondToMessage("The next claim event is in: " + CacheEventController.getLastEventTime() + " minutes", commandEvent);
        } else {
            Helper.respondToMessage("You do not have the required Bot perms to do that.", commandEvent);
        }
    }
}
