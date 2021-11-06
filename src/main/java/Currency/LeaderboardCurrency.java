package Currency;

import Util.Helper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class LeaderboardCurrency extends Command {

    public LeaderboardCurrency() {
        this.name = "Leaderboard";
        this.aliases = new String[]{"leader", "board", "lb", "score"};
        this.arguments = "<board>";
        this.children = new Command[]{new LeaderboardTop()};
        this.help = "Shows the Leaderboards";
        this.category = Helper.currency;
    }

    //BAae command for further leaderboard commands
    @Override
    protected void execute(CommandEvent event) {
        Helper.respondToMessage("Use (top) with this command", event);
    }

}
