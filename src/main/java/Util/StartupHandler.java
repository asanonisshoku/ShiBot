package Util;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class StartupHandler extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        ServerInfo serverInfo = new ServerInfo(event.getGuild().getId(), "null");
        MongoHandler.saveServerInfoToDatabase(serverInfo);
    }
}
