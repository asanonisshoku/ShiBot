package Currency.Games;

//Users can mine for currency by sending !mine for 3-15 coins a message
//rate limit?

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Random;

public class CurrencyMine extends Command {
    Random random = new Random();

    public CurrencyMine() {
        this.name = "mine";
        this.aliases = new String[]{"dig"};
        this.help = "mine some shicoin's from deep within the planet";
        this.cooldown = 5;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().length() > 0) {

        } else {
            int coinsAwarded = random.nextInt(13) + 3;

            BotUser botUser = MongoHandler.getUser(event.getMessage().getAuthor().getId());
            botUser.addCurrency(coinsAwarded);

            Helper.setEffectiveNameToUser(event, botUser);

            Helper.respondToMessage(botUser.getEffectiveName() + " has mined " + coinsAwarded + " shicoins", event);

            MongoHandler.saveUserToDatabase(botUser);
        }
    }
}
