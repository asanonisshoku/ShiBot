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

    //leaderboard
    //give currency for admin
    //take currency for admin
    //claim daily
    //Handles Basic money print out for users on request in a discord channel.


    @Override
    protected void execute(CommandEvent event) {

        if (arguments != null){
            return;
        }

        BotUser botUser = MongoHandler.getUser(event.getMessage().getAuthor().getId());

        Helper.respondToMessage("You have " + botUser.getCurrency() + " shicoins", event);
        Helper.setEffectiveNameToUser(event, botUser);
        MongoHandler.saveUserToDatabase(botUser);

        if (CacheEventController.isStartup()){
            CacheEventController.startEventStartUp(event);
        }
    }
}
