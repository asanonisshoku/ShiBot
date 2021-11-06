package Currency.Games;

import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class VtuberStatCheck extends Command {

    //A way for a user to see the stats of a gladiator if they are unsure on who to pick

    public VtuberStatCheck() {
        this.name = "stat check";
        this.aliases = new String[]{"vtuber", "statcheck", "vtuberstats"};
        this.help = "check the stats of any vtuber";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();

        //Query from the database and turn relevant info into a discord embedded message
        VtuberGladiator vtuberGladiator = MongoHandler.queryVtuberFromDataBase(event.getArgs());

        builder.addField("Name", vtuberGladiator.getName(), false);
        builder.addField("Strength","" +  vtuberGladiator.getStrength(), false);
        builder.addField("Speed", "" + vtuberGladiator.getSpeed(), false);
        builder.addField("Defence","" + vtuberGladiator.getDefence(), false);
        builder.addField("Wins", "" + vtuberGladiator.getTotalWins(), false);

        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
