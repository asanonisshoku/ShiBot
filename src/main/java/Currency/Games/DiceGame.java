package Currency.Games;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import Util.ServerInfo;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.util.Random;

public class DiceGame extends Command {
    Random random = new Random();

    public DiceGame() {
        this.name = "dice";
        this.aliases = new String[]{"dice", "roll"};
        this.arguments = "<amount>";
        this.help = "A playable dice game to lose your money in";
    }


    //TODO Factor in karma
    @Override
    protected void execute(CommandEvent event) {

        BotUser botUser = MongoHandler.getUser(event.getMessage().getAuthor().getId());

        String[] arguments = event.getArgs().split("\\s+");

        if (arguments.length > 0) {

            String argumment = arguments[0];

            int bet = 0;

            if (argumment.equals("all")){
                bet = botUser.getCurrency();
                botUser.removeCurrency(bet);
            }
            else if (argumment.equals("half")){
                bet = botUser.getCurrency()/2;
                botUser.removeCurrency(bet);
            }
            else {
                try {
                    int x = Integer.parseInt(argumment);

                    //Negative int fallback won't process command if its a negative number.

                    if (x <= 0){
                        Helper.respondToMessage("You cannot bet " + x, event);
                        return;
                    }

                    if (botUser.getCurrency() >= x) {
                        botUser.removeCurrency(x);
                        bet = x;
                    }
                    else {
                        Helper.respondToMessage("Not enough shicoins to bet " + x, event);
                        return;
                    }
                } catch (Exception e){
                    Helper.respondToMessage("Invalid amount use <amount> or <all/half>", event);
                    return;
                }
            }

            if (bet == 0){
                Helper.respondToMessage("You cannot bet 0 shicoins", event);
                return;
            }

            int resultOfRoll = random.nextInt(100) + 1;

            StringBuilder builder = new StringBuilder();

            builder.append("Rolled " + resultOfRoll + "/100, ");

            if (resultOfRoll <= 50){
                builder.append("You lost " + bet + " shicoins better luck next time! ");
            }
            else if (resultOfRoll <= 99){
                builder.append("You won " + (bet * 2) + " shicoins! ");
                botUser.addCurrency((bet * 2));
            }
            else {
                int secretNumber = random.nextInt(16) + 1;

                if (secretNumber == 16){
                    ServerInfo serverInfo = MongoHandler.getServerInfo(event);
                    builder.append("Jackpot! The Cat Goddess herself gives your her offerings you won " + ((bet * 16) + serverInfo.getCatGodCoins()) + " shicoins! ");
                    botUser.addCurrency((bet * 16) + serverInfo.getCatGodCoins());
                    serverInfo.setCatGodCoins(0);
                    MongoHandler.saveServerInfoToDatabase(serverInfo);
                    System.out.println("Jackpot payed out");
                }else {
                    builder.append("Grand Prize, you won " + (bet * 4) + " shicoins! ");
                    botUser.addCurrency((bet * 4));
                }
            }

            int currentCurrencyBalance = botUser.getCurrency();

            Helper.respondToMessage(builder.toString() + "Your current shicoin balance is " + currentCurrencyBalance, event);

            Helper.setEffectiveNameToUser(event, botUser);
            MongoHandler.saveUserToDatabase(botUser);
        }
    }


}
