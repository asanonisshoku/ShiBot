package Admin;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemoveAdmin extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        Member messageSender = event.getMember();
        if (Helper.messageReceived("removeAdmin", event) && messageSender.hasPermission(Permission.ADMINISTRATOR) || Helper.messageReceived("removeAdmin", event) && MongoHandler.getUser(Helper.getUserID(event)).isAdmin()){
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            if (!mentionedUsers.isEmpty()) {
                for (User user : mentionedUsers) {
                    if (!user.getId().equals(messageSender.getId())) {
                        BotUser mongoBotUser = MongoHandler.getUser(user.getId());
                        mongoBotUser.setAdmin(false);
                        MongoHandler.saveUserToDatabase(mongoBotUser);
                        Helper.queueMessage("Removed " + user.getName() + " a Bot Admin", event);
                    }
                }
            }
            else {
                Helper.queueMessage("Please mention the user/users you would like to remove Bot Admin to", event);
            }
        }
    }
}
