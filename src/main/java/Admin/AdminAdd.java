package Admin;

import Util.BotUser;
import Util.MongoHandler;
import Util.Helper;
import Util.ServerInfo;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class AdminAdd extends Command {

    public AdminAdd() {
        this.name = "admin add";
        this.aliases = new String[]{"add", "op"};
        this.arguments = "<@User>";
        this.help = "Bot Admins a user for bot perms";
    }

    //Allows a server admin to mod someone with bot permissions without giving them admin access in the discord group.
    @Override
    protected void execute(CommandEvent commandEvent) {
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(commandEvent));

        if (Helper.hasBotPerms(commandEvent, botUser)) {
            List<net.dv8tion.jda.api.entities.User> mentionedUsers = commandEvent.getMessage().getMentionedUsers();
            ServerInfo serverInfo = MongoHandler.getServerInfo(commandEvent);

            if (!mentionedUsers.isEmpty()) {
                for (User user : mentionedUsers) {
                    BotUser mongoBotUser = MongoHandler.getUser(user.getId());

                    mongoBotUser.setAdmin(true);
                    MongoHandler.saveUserToDatabase(mongoBotUser);

                    Helper.respondToMessage("Made " + user.getName() + " a Bot Admin", commandEvent);
                    System.out.println("User " + mongoBotUser.getId() + "added as a bot admin in server, " + serverInfo.getServerID());
                }
            } else {
                Helper.respondToMessage("Please mention the user/users you would like to give Bot Admin to", commandEvent);
            }
        } else {
            Helper.respondToMessage("You do not have the Bot Perms to do that.", commandEvent);
        }
    }
}
