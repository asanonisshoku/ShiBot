package Admin;

import Util.BotUser;
import Util.MongoHandler;
import Util.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddAdmin extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Member messageSender = event.getMember();
        if (Helper.messageReceived("addAdmin", event) || Helper.messageReceived("addAdmin", event)){
            BotUser botUser =  MongoHandler.getUser(Helper.getUserID(event));
            if (botUser.isAdmin() || messageSender.hasPermission(Permission.ADMINISTRATOR)) {
                List<net.dv8tion.jda.api.entities.User> mentionedUsers = event.getMessage().getMentionedUsers();
                if (!mentionedUsers.isEmpty()) {
                    for (User user : mentionedUsers) {
                        BotUser mongoBotUser = MongoHandler.getUser(user.getId());
                        mongoBotUser.setAdmin(true);
                        System.out.println(mongoBotUser.isAdmin());
                        MongoHandler.saveUserToDatabase(mongoBotUser);
                        Helper.queueMessage("Made " + user.getName() + " a Bot Admin", event);
                        System.out.println("User " + mongoBotUser.getId() + "added as a bot admin");
                    }
                } else {
                    Helper.queueMessage("Please mention the user/users you would like to give Bot Admin to", event);
                }
            }else {
                Helper.queueMessage("You do not have the Bot Perms to do that.", event);
            }
        }
        else if (Helper.messageReceived("adminstatus", event)){
            BotUser botUser = MongoHandler.getUser(Helper.getUserID(event));
            Helper.queueMessage(botUser.isAdmin() + "", event);
        }
    }
}
