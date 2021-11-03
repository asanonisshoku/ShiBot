package Currency.Games;

import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class VtuberStatCheck extends Command {
    //A way for a user to see the stats of gladiators

    public VtuberStatCheck() {
        this.name = "stat check";
        this.aliases = new String[]{"vtuber", "statcheck", "vtuberstats"};
        this.help = "check the stats of any vtuber";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();

        VtuberGladiator vtuberGladiator = MongoHandler.queryVtuberFromDataBase(event.getArgs());

        builder.addField("Name", vtuberGladiator.getName(), false);
        builder.addField("Strength","" +  vtuberGladiator.getStrength(), false);
        builder.addField("Speed", "" + vtuberGladiator.getSpeed(), false);
        builder.addField("Defence","" + vtuberGladiator.getDefence(), false);
        builder.addField("Wins", "" + vtuberGladiator.getTotalWins(), false);

        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
