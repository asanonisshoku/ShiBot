package Currency;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ViewCurrencyAdmin extends Command{

    public ViewCurrencyAdmin() {
        this.name = "shicoin admin view";
        this.aliases = new String[]{"user"};
        this.arguments = "<user>";
        this.help = "show's how much currency a selected user has.";
        this.category = Helper.admin;
    }

    //Admin command to allow an admin to check how much currency a mentioned user has
    @Override
    protected void execute(CommandEvent event) {

        BotUser botUser = MongoHandler.getUser(event.getMessage().getAuthor().getId());

        String[] arguments = event.getArgs().split("\\s+");

        if (arguments.length > 0 && Helper.hasBotPerms(event, botUser)) {

            BotUser target = null;

            try {
                target = MongoHandler.getUser(event.getMessage().getMentionedUsers().get(0).getId());
            } catch (Exception e){
                Helper.respondToMessage("Please mention a user to view their shicoin", event);
                return;
            }

            Helper.respondToMessage(Helper.getDefaultUserName(event, target) + " has " + target.getCurrency() + " shicoin", event);
            }
        }

}
