package CatGoddess;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import Util.ServerInfo;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Pray extends Command {

    public Pray() {
        this.name = "pray";
        this.aliases = new String[]{"nia", "donate", "pray", "dono", "donowall"};
        this.arguments = "<amount>";
        this.help = "Pray and donate to the cat Goddess";
    }

    //Pray to the cat god
    //donate to the cat goddess, possible Rewards? Small chance to get current cat god coins with donos over 1000?
    //Nice message from followers of the goddess
    //Total held donation jackpot

    //Karma? Fortunes?


    //A small command to let a user donate to a "cat goddess", another way for a user to spend currency while feeling good about it, donations go towards the dice game mega jackpot with possible expansions in this system later on
    @Override
    protected void execute(CommandEvent event) {

        BotUser botUser = MongoHandler.getUser(event.getMessage().getAuthor().getId());

        String[] arguments = event.getArgs().split("\\s+");

        if (arguments.length > 0) {
            int x;

            try {
                x = Integer.parseInt(arguments[0]);
            } catch (Exception e){
                Helper.respondToMessage("You cannot donate 0 shicoins, the goddess always requires a donation of at least 1", event);
                return;
            }

            if (botUser.getCurrency() < 0){
                return;
            }

            //User and server Database Handling
            ServerInfo serverInfo = MongoHandler.getServerInfo(event);

            serverInfo.setCatGodCoins(serverInfo.getCatGodCoins() + x);
            serverInfo.setCatGodTotalCoins(serverInfo.getCatGodTotalCoins() + x);

            MongoHandler.saveServerInfoToDatabase(serverInfo);

            botUser.removeCurrency(x);

            Helper.setEffectiveNameToUser(event, botUser);

            //Adding unused karma for use later on.
            //Karma being a multiplier for winnings in some games and to make a user feel good about themselves.
            botUser.setKarma(botUser.getKarma() + ((x/100)/100));
            MongoHandler.saveUserToDatabase(botUser);

            Helper.respondToMessage("You get the feeling that The Cat Goddess thanks you for your generous donation of " + x + " shicoins, the temple's followers praise your generous donation and thank you for your kindness." , event);
        }
    }
}
