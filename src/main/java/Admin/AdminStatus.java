package Admin;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class AdminStatus extends Command {

    //Small helper class for checking is if a bot properly admin'ed someone or if you un admin'ed someone you don't want to have bot perms
    public AdminStatus() {
        this.name = "admin status";
        this.aliases = new String[]{"status", "isadmin"};
        this.arguments = "<@User>";
        this.help = "Checks if a user is a Bot Admin Bot";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

        BotUser botUser =  MongoHandler.getUser(Helper.getUserID(commandEvent));

        if (Helper.hasBotPerms(commandEvent, botUser)){
            User mentionedUser;

            try {
                mentionedUser = commandEvent.getMessage().getMentionedUsers().get(0);
            } catch (Exception e){
                Helper.respondToMessage("No Valid Mentioned User, please try again", commandEvent);
                return;
            }

            BotUser mentionedBotUser = MongoHandler.getUser(mentionedUser.getId());

            String flag = "" + mentionedBotUser.isAdmin();

            if (mentionedBotUser.isAdmin()){
                Helper.respondToMessage(flag + " ,is a Admin", commandEvent);
            } else {
                Helper.respondToMessage(flag + " ,isnt a Admin", commandEvent);
            }
        } else {
            Helper.respondToMessage("You do not have the Bot Perms to check that.", commandEvent);
        }
    }
}
