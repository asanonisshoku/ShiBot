package Currency;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class RemoveCurrency extends Command {
    public RemoveCurrency() {
        this.name = "remove";
        this.aliases = new String[]{"remove", "take"};
        this.arguments = "<user> <amount>";
        this.help = "Bot Admin command to remove some currency from the user";
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
                        target.removeCurrency(Integer.parseInt(arguments[0]));

                        if (target.getCurrency() <= 0){
                            target.setCurrency(0);
                        }
                    } catch (Exception e){
                        Helper.respondToMessage("Error getting amount to take " + x, event);
                        return;
                    }
                    MongoHandler.saveUserToDatabase(target);
                    Helper.respondToMessage("Took " + Integer.parseInt(arguments[0]) + " shicoins from user " + x, event);
                    i++;
                }
            } catch (Exception e){
                Helper.respondToMessage("No User Mentioned to take shicoins from.", event);
            }
        }
        else {
            Helper.respondToMessage("You do not have the Bot Perms to do that.", event);
        }
    }
}
