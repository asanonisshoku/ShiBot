package Admin;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ChannelID extends Command {

    public ChannelID() {
        this.name = "channel";
        this.aliases = new String[]{"channel", "id", "cid"};
        this.help = "gets a channel ID";
    }

    //A helper command to allow a admin to get the id if the current discord channel they are in to use for setting bot only channels and bot game/event channels.
    @Override
    protected void execute(CommandEvent commandEvent) {
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(commandEvent));

        if (Helper.hasBotPerms(commandEvent, botUser)){
            Helper.respondToMessage("Current Channel ID: " + commandEvent.getChannel().getId(), commandEvent);
        } else {
            Helper.respondToMessage("You do not have the required Bot perms to do that.", commandEvent);
        }
    }
}
