package Admin;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Admin extends Command {

    public Admin() {
        this.name = "admin";
        this.aliases = new String[]{"admin", "op"};
        this.children = new Command[]{new AdminStatus(), new AdminAdd(), new AdminRemove()};
        this.guildOnly = true;
    }

    //Base command for other admin commands to be used

    @Override
    protected void execute(CommandEvent commandEvent) {

        BotUser botUser = MongoHandler.getUser(Helper.getUserID(commandEvent));

        //Failsafe if a so an non-admin'ed user can't use command at all
        if (Helper.hasBotPerms(commandEvent, botUser)){
            Helper.respondToMessage("Please use s!admin help to view available commands", commandEvent);
        } else {
            Helper.respondToMessage("You do not have the required Bot perms to do that.", commandEvent);
        }
    }
}
