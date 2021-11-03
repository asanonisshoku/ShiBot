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


    @Override
    protected void execute(CommandEvent event) {
        BotUser botUser = MongoHandler.getUser(Helper.getUserID(event));
        if (Helper.canClaim(botUser)){
            botUser.addCurrency(100);
            botUser.setLastClaim(System.currentTimeMillis());
            Helper.respondToMessage("You have claimed 100 Shicoin, You now have " + botUser.getCurrency() + " Shicoin", event);
        }else {
            Helper.respondToMessage("Please wait " + Helper.timeTillClaim(botUser) + " to claim again", event);
        }
        Helper.setEffectiveNameToUser(event, botUser);
        MongoHandler.saveUserToDatabase(botUser);
    }
}
