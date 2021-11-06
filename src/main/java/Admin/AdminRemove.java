package Admin;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import Util.ServerInfo;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminRemove extends Command {

    public AdminRemove() {
        this.name = "admin remove";
        this.aliases = new String[]{"remove", "deop"};
        this.arguments = "<@User>";
        this.help = "remove Bot Admin from a user for bot perms";
    }

    //Admin command to remove bot perms from a un discord group admin'ed person
    @Override
    protected void execute(CommandEvent commandEvent) {
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(commandEvent));

        if (Helper.hasBotPerms(commandEvent, botUser)) {
            List<net.dv8tion.jda.api.entities.User> mentionedUsers = commandEvent.getMessage().getMentionedUsers();
            ServerInfo serverInfo = MongoHandler.getServerInfo(commandEvent);

            if (!mentionedUsers.isEmpty()) {
                for (User user : mentionedUsers) {
                    BotUser mongoBotUser = MongoHandler.getUser(user.getId());

                    mongoBotUser.setAdmin(false);
                    MongoHandler.saveUserToDatabase(mongoBotUser);

                    Helper.respondToMessage("Removed " + user.getName() + " as Bot Admin", commandEvent);
                    System.out.println("User " + mongoBotUser.getId() + "removed as a bot admin in server, " + serverInfo.getServerID());
                }
            } else {
                Helper.respondToMessage("Please mention the user/users you would like to remove Bot Admin from", commandEvent);
            }
        } else {
            Helper.respondToMessage("You do not have the Bot Perms to do that.", commandEvent);
        }
    }
}
