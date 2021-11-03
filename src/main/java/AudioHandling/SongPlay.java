package AudioHandling;

import Util.Helper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SongPlay extends Command {

    public SongPlay() {
        this.name = "play";
        this.aliases = new String[]{"play"};
        this.arguments = "<song url>";
        this.help = "Play a song from the url";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs() != null){
            String[] arguments = event.getArgs().split("\\s+");

            if (arguments.length > 1){
                Helper.respondToMessage("Too many arguments, just add the url", event);
                return;
            }

            //TODO Add Audio Handler
        } else {
            Helper.respondToMessage("Please provide a valid URL", event);
        }
    }
}
