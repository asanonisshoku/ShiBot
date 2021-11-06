package Currency;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class GiftCurrency extends Command {

    public GiftCurrency() {
        this.name = "gift";
        this.aliases = new String[]{"gift"};
        this.arguments = "<amount> <user>";
        this.category = Helper.currency;
        this.help = "gift your shicoin to another user.";
    }

    //Command that allows a user to gift some of their currency to another user
    @Override
    protected void execute(CommandEvent event) {

        String[] arguments = event.getArgs().split("\\s+");


        //Restricts the user to only mention one user to gift too.
        if (arguments.length < 2) {
            Helper.respondToMessage("Not enough arguments use " + this.arguments, event);
        }
        if (arguments.length > 2){
            Helper.respondToMessage("Too many arguments use " + this.arguments, event);
        }

        //Error Checking
        User user;
        try {
            user = event.getMessage().getMentionedUsers().get(0);
        } catch (Exception e) {
            Helper.respondToMessage("No User Mentioned to gift shicoin to", event);
            return;
        }

        BotUser target = MongoHandler.getUser(user.getId());
        BotUser messageAuthor = MongoHandler.getUser(event.getAuthor().getId());

        String targetName = Helper.getDefaultUserName(event, target);

        try {
            int y = Integer.parseInt(arguments[0]);

            if (MongoHandler.getUser(event.getAuthor().getId()).getCurrency() >= y){

                target.addCurrency(y);
                messageAuthor.removeCurrency(y);

                MongoHandler.saveUserToDatabase(messageAuthor);
                MongoHandler.saveUserToDatabase(target);

                Helper.respondToMessage("you gifted " + y + " shicoin to " + targetName, event);
            }else {
                Helper.respondToMessage("you do not have enough shicoin to do that.", event);
            }
        } catch (Exception e) {
            Helper.respondToMessage("Error in amount of shicoin to gift please use " + this.arguments, event);
        }
}
}
