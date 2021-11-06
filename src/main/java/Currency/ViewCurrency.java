package Currency;

import Currency.Events.CacheEventMisc.CacheEventController;
import Util.Helper;
import Util.MongoHandler;
import Util.BotUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;


public class ViewCurrency extends Command {

    public ViewCurrency() {
        this.name = "shicoin";
        this.aliases = new String[]{"shicoin", "shi", "gil", "money", "currency"};
        this.children = new Command[]{new AddCurrency(), new RemoveCurrency(), new ClaimCurrency(), new GiftCurrency(), new ViewCurrencyAdmin()};
        this.help = "show's how much currency a user has.";
        this.category = Helper.currency;
    }

    //Handles Basic money print out for users on request in a discord channel.


    //Command for a user to see their current currency amount.
    @Override
    protected void execute(CommandEvent event) {

        //Only runs if there is no other arguments as this is also
        if (arguments != null){
            return;
        }

        BotUser botUser = MongoHandler.getUser(event.getMessage().getAuthor().getId());

        Helper.respondToMessage("You have " + botUser.getCurrency() + " shicoins", event);

        //Save users effective name to database for future use
        Helper.setEffectiveNameToUser(event, botUser);

        MongoHandler.saveUserToDatabase(botUser);

        //Triggers the cache event to spawn for the first time only when a user uses the bot to save resources
        if (CacheEventController.isStartup()){
            CacheEventController.startEventStartUp(event);
        }
    }
}
