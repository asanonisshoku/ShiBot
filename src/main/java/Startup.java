import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class Startup implements EventListener {

    public static void start() {
        System.out.println("Startup Successful");
    }


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent){
            System.out.println("Startup Successful");
        }
    }
}
