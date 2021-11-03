package Currency;

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import Util.ServerInfo;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class LeaderboardTop extends Command {
    public LeaderboardTop() {
        this.name = "leaderboard top";
        this.aliases = new String[]{"top"};
        this.help = "The Top Leaderboard";
        this.category = Helper.currency;
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();

        ServerInfo serverInfo = MongoHandler.getServerInfo(event);
        System.out.println(serverInfo.getServerID());

        builder.setColor(Color.MAGENTA);
        builder.setFooter("Top " + "Shicoins" + " for " + event.getGuild().getName());
        int i = 1;
        for (BotUser user: MongoHandler.getTopUserFiveCurrency(serverInfo)){
            builder.addField(new MessageEmbed.Field("#" + i + " " + user.getEffectiveName(), "" + user.getCurrency(), false));
            i++;
        }
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
