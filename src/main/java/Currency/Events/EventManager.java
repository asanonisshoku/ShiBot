package Currency.Events;

import Util.Helper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class EventManager extends Command {
    public EventManager() {
        this.name = "event";
        this.aliases = new String[]{"e", "event", "events"};
        this.children = new Command[]{new CacheEvent()};
        this.help = "Event Prefix";
    }

    @Override
    protected void execute(CommandEvent event) {
        Helper.respondToMessage("Use with <event> <action>", event);
    }
}
