package Currency.Events;

import Currency.Events.CacheEventMisc.CacheEventController;
import Util.Helper;
import Util.MongoHandler;
import Util.ServerInfo;
import Util.BotUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class CacheEvent extends Command {

    public CacheEvent() {
        this.name = "Cache Event";
        this.aliases = new String[]{"cache", "cacheevent", "ce", "dropevent", "drop"};
        this.arguments = "<action>";
        this.help = "Cache style event that drops currency randomly on the ground";
    }


    //Cache Event Command settings for an admin to use to tweak settings of the cache event or to force/reset it.
    @Override
    protected void execute(CommandEvent event) {
        BotUser botUser = MongoHandler.getUser(event.getAuthor().getId());

        String[] arguments = event.getArgs().split("\\s+");

        if (arguments.length > 2){
            Helper.respondToMessage("Not enough arguments use " + this.arguments, event);
        }

        else if (Helper.hasBotPerms(event, botUser)) {
            String arg = arguments[0];

            if (arg.equals("force")){
                CacheEventController.setEventActive(true);

                ServerInfo serverInfo = MongoHandler.getServerInfo(event);

                CacheEventController.announceEvent(event, serverInfo);
            }
            else if (arg.equals("reset")){
                CacheEventController.resetEvent(event);
                Helper.respondToMessage("Reset Event", event);
            }
            else if (arg.equals("multiplier")){
                try {
                    CacheEventController.setTimeToEvent(Integer.parseInt(arguments[1]));
                }
                catch (Exception e){
                    Helper.respondToMessage("Invalid argument use : multiplier <amount>", event);
                }
            }
            else if (arg.equals("prize")){
                try {
                    CacheEventController.setPrize(Integer.parseInt(arguments[1]));
                }
                catch (Exception e){
                    Helper.respondToMessage("Invalid argument use : prize <amount>", event);
                }
            }
            else if (arg.equals("timeToEvent")){
                Helper.respondToMessage("Next cache event in " + CacheEventController.getTimeToEvent() + " minutes", event);
            }
            else if (arg.equals("frequency")){}
            else if (arg.equals("setEventChannel")){
                ServerInfo serverInfo = MongoHandler.getServerInfo(event);

                serverInfo.setEventChannelID(event.getChannel().getId());

                Helper.respondToMessage("You have set " + event.getChannel().getName() + " as the channel the cache event happens in", event);
                MongoHandler.saveServerInfoToDatabase(serverInfo);
            }
            else {
                Helper.respondToMessage("No other operations except are supported at this time, in the future more options will be included", event);
            }
        }
        else {
            Helper.respondToMessage("You need to be a Bot Admin to preform this action.", event);
        }
    }

}
