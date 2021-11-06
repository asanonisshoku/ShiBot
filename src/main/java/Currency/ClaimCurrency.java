package Currency;

import Util.Helper;
import Util.MongoHandler;
import Util.BotUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ClaimCurrency extends Command {

    public ClaimCurrency() {
        this.name = "Claim";
        this.aliases = new String[]{"claim", "daily"};
        this.help = "Claim some Shicoin every 24 hours";
    }


    //Command for a user to claim a daily currency allowance
    @Override
    protected void execute(CommandEvent event) {

        BotUser botUser = MongoHandler.getUser(Helper.getUserID(event));

        if (Helper.canClaim(botUser)){

            botUser.addCurrency(100);
            botUser.setLastClaim(System.currentTimeMillis());

            Helper.respondToMessage("You have claimed 100 Shicoin, You now have " + botUser.getCurrency() + " Shicoin", event);

        }else {
            //a user can only claim currency every 24 hours, if they can claim it yet returns the time until they can claim again.
            Helper.respondToMessage("Please wait " + Helper.timeTillClaim(botUser) + " to claim again", event);
        }

        //Grab users effective name and save it to the database for future use
        Helper.setEffectiveNameToUser(event, botUser);

        MongoHandler.saveUserToDatabase(botUser);
    }
}
