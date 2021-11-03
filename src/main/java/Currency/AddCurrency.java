package Currency;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class AddCurrency extends Command {
    public AddCurrency() {
        this.name = "Add Shicoin";
        this.aliases = new String[]{"add", "give"};
        this.arguments = "<amount> <user>...";
        this.help = "Bot Admin command to give a user some currency";
        this.category = Helper.admin;
    }

    @Override
    protected void execute(CommandEvent event) {
        BotUser botUser = MongoHandler.getUser(event.getAuthor().getId());

        String[] arguments = event.getArgs().split("\\s+");

        if (arguments.length < 2){
            Helper.respondToMessage("Not enough arguments use " + this.arguments, event);
        }

        try {
            //Basic check to make sure user is doing command correctly
            int check = Integer.parseInt(arguments[0]);
        } catch (Exception e){
            Helper.respondToMessage("Not enough arguments use " + this.arguments, event);
        }

        if (Helper.hasBotPerms(event, botUser)){
            int i = 0;
            try {
                for (User user: event.getMessage().getMentionedUsers()){
                    BotUser target = MongoHandler.getUser(user.getId());
                    String x = Helper.getUserName(event, target, i);
                    try {
                        target.addCurrency(Integer.parseInt(arguments[0]));
                    } catch (Exception e){
                        Helper.respondToMessage("Error getting amount to give " + x, event);
                        return;
                    }
                    MongoHandler.saveUserToDatabase(target);
                    Helper.respondToMessage("Gave " + Integer.parseInt(arguments[0]) + " shicoins to user " + x, event);
                    i++;
                }
            } catch (Exception e){
                Helper.respondToMessage("No User Mentioned to give shicoins too", event);
            }
        }
        else {
            Helper.respondToMessage("You do not have the Bot Perms to do that.", event);
        }
    }
}
