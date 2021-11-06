package Currency.Events;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class EventManager extends Command {
    public EventManager() {
        this.name = "event";
        this.aliases = new String[]{"e", "event", "events"};
        this.children = new Command[]{new CacheEvent()};
        this.help = "Event Prefix";
    }

    //A base command to use for further event actions
    @Override
    protected void execute(CommandEvent commandEvent) {
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(commandEvent));

        //Failsafe if a so an non-admin'ed user can't use command at all
        if (Helper.hasBotPerms(commandEvent, botUser)){
            Helper.respondToMessage("Use with <event> <action>", commandEvent);
        } else {
            Helper.respondToMessage("You do not have the required Bot perms to do that.", commandEvent);
        }
    }
}
