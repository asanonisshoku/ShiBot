package Currency;

import Util.Helper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class LeaderboardCurrency extends Command {
    //Global top currency across all shards

    public LeaderboardCurrency() {
        this.name = "Leaderboard";
        this.aliases = new String[]{"leader", "board", "lb", "score"};
        this.arguments = "<board>";
        this.children = new Command[]{new LeaderboardTop()};
        this.help = "Shows the Leaderboards";
        this.category = Helper.currency;
    }

    @Override
    protected void execute(CommandEvent event) {
        Helper.respondToMessage("Use (top, global) with this command", event);
    }

    //All Server Top
}
