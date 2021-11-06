package Admin;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TestCommands extends Command {

    public TestCommands() {;
        this.name = "runVtuberSetup";
        this.help = "Bot Dev Command";
    }

    //Blank dev command to allow dev to cause changes in databases or other misc commands as needed during dev
    @Override
    protected void execute(CommandEvent event) {
        //TODO Update all users in database
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(event));

        //Failsafe if a so an non-admin'ed user can't use command at all
        if (Helper.hasBotPerms(event, botUser)){
            Helper.respondToMessage("NA", event);
        } else {
            Helper.respondToMessage("You do not have the required Bot perms to do that.", event);
        }
    }
}
