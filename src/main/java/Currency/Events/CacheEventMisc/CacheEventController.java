package Currency.Events.CacheEventMisc;

import Util.Helper;
import Util.MongoHandler;
import Util.ServerInfo;
import Util.BotUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CacheEventController extends Command {

    //Drops a claim currency in a random bot channel for an extra currency claim based on chosen variables with in the future expansions onto it to allow a discord group owner to tweak these to their liking.
    private static Timer timer = new Timer();
    private static Timer catGoddess = new Timer();


    private static Random random = new Random();


    //TODO on startup set vars from serverinfo mongo object
    private static int prize = 350;

    private static long eventTimer = 0;
    private static long lastEventTime = 0;

    //Base time of 6 hours until an event spawns
    private static int timeToEvent = 6;

    private static boolean startup = true;

    //A admin chosen Multiplier for bonuses this can also change dynamically on luck aka Karma(Cat Goddess) or how long it has been since the money was last picked up.
    private static double multiplier = 1.0;
    private static double goddessMultiplier = 0;
    private static double adminChosenMultiplier = 1.0;

    private static boolean eventActive = false;

    public CacheEventController() {
        this.name = "event cache";
        this.aliases = new String[]{"snatch"};
        this.help = "A random event where you find dropped currency on the ground, the first user that finds it keeps it.";
    }

    //TODO Be able to sense when next drop is
    //Event Claim logic/command for when a user actually claims a spawned event.
    @Override
    protected void execute(CommandEvent event) {

        if (eventActive) {

            ServerInfo serverInfo = MongoHandler.getServerInfo(event);

            //Check to make sure the channel the event is getting claimed in is the admin selected channel
            if (event.getMessage().getChannel().getId().equals(event.getGuild().getTextChannelById(serverInfo.getEventChannelID()).getId())) {

                BotUser botUser = MongoHandler.getUser(Helper.getUserID(event));

                //User handling to claim the prize
                claimCurrency(event, botUser);
                //TODO Factor in User Karma for reward bonus

                //Calculate what the next prize will be for the event, base is min 100 currency max 500 currency then factor in admin multipliers
                prize = (int) ((random.nextInt(400) + 100) * calculateMultiplier(1.0));
                eventActive = false;

                //Calculate when the next event should run, min of 8 hours
                CacheEventController.eventTimer = (long) random.nextInt(timeToEvent * 60 * 60 * 1000) + 21600000;
                CacheEventController.lastEventTime = System.currentTimeMillis();

                //Catch the users effective name on claim and saves it to the database for future use
                Helper.setEffectiveNameToUser(event, botUser);

                //starts a new timer for the next event.
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        announceEvent(event, serverInfo);
                        eventActive = true;
                        MongoHandler.saveServerInfoToDatabase(serverInfo);
                    }
                }, eventTimer);

                //Auto claims and raises Multiplier every 20 mins.
                catGoddess.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //After 20 mins the drop should disappear
                        if (eventActive) {
                            catGoddessClaim(event, serverInfo);
                        }
                    }
                }, 20 * 60 * 1000);
            }
        }
    }
    private static Timer startTimer = new Timer();

    //Starts event on a bot reboot everytime the s!gil command is called
    public static void startEventStartUp(CommandEvent event){
        ServerInfo serverInfo = MongoHandler.getServerInfo(event);

        //small amount of randomness so the user doesn't always notice that is what activates the event on reboot
        int i = random.nextInt(147);

        TextChannel channel = event.getGuild().getTextChannelById(serverInfo.getEventChannelID());

        startTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    channel.sendMessage("Someone dropped " + prize + " shicoins here, Type s!snatch to claim it before anyone else.").queue();
                                }
                            }, i * 1000);

        eventActive = true;
        startup = false;
    }

    public static void announceEvent(CommandEvent event, ServerInfo serverInfo){
        TextChannel channel = event.getGuild().getTextChannelById(serverInfo.getEventChannelID());
        channel.sendMessage("Someone dropped " + prize + " shicoins here, Type s!snatch to claim it before anyone else.").queue();
    }


    //TODO Debug cat goddess claim the rest action isnt queueing
    private static void catGoddessClaim(CommandEvent event, ServerInfo serverInfo){
        TextChannel channel = event.getGuild().getTextChannelById(serverInfo.getEventChannelID());

        channel.sendMessage("No one claimed the shicoin and the Cat Goddesses loyal followers found it for donation they have found " + serverInfo.getCatGodCoins() + ", better luck next time.").queue();

        System.out.println("Cat Goddess Claim");
        multiplier += 0.1;
        serverInfo.setCatGodCoins(serverInfo.getCatGodCoins() + prize);
        serverInfo.setCatGodTotalCoins(serverInfo.getCatGodTotalCoins() + prize);
        eventActive = false;
    }

    //User handling to claim the current prize and save the user to the database
    private void claimCurrency(CommandEvent event, BotUser botUser){
        Helper.respondToMessage("You claimed " + prize + " shicoins", event);
        System.out.println(prize + " shicoins claimed by: " + botUser.getId() + " during event.");
        botUser.addCurrency(prize);
        goddessMultiplier = 0;

        MongoHandler.saveUserToDatabase(botUser);
    }

    //Forces a Cache event to spawn.
    @Deprecated
    public void forceCacheEvent(CommandEvent event){
        Helper.respondToMessage("Forcing event...", event);
        eventActive = true;

        ServerInfo serverInfo = MongoHandler.getServerInfo(event);

        announceEvent(event, serverInfo);
        execute(event);
    }

    public static void resetEvent(CommandEvent event){
        ServerInfo serverInfo = MongoHandler.getServerInfo(event);

        timer.cancel();
        catGoddess.cancel();

        CacheEventController.eventTimer = (long) random.nextInt(timeToEvent * 60 * 60 * 1000) + 21600000;
        CacheEventController.lastEventTime = System.currentTimeMillis();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                announceEvent(event, serverInfo);
                eventActive = true;
                MongoHandler.saveServerInfoToDatabase(serverInfo);
            }
        }, eventTimer);

        catGoddess.schedule(new TimerTask() {
            @Override
            public void run() {
                //After 20 mins the drop should disappear
                if (eventActive) {
                    catGoddessClaim(event, serverInfo);
                }
            }
        }, 20 * 60 * 1000);
    }

    public static void startCatGoddessTimer(CommandEvent event, ServerInfo serverInfo){
        catGoddess.schedule(new TimerTask() {
        @Override
        public void run() {
            //After 20 mins the drop should disappear
            if (eventActive) {
                catGoddessClaim(event, serverInfo);
            }
        }
    }, 10 * 1000);}

    public static String getTimeToEvent(){
        return "" + TimeUnit.MILLISECONDS.toMinutes((CacheEventController.lastEventTime + CacheEventController.eventTimer) - CacheEventController.lastEventTime);
    }

    private double calculateMultiplier(double karma){
        return (adminChosenMultiplier + goddessMultiplier);
    }

    public static boolean isEventActive() {
        return eventActive;
    }

    public static void setEventActive(boolean eventActive) {
        CacheEventController.eventActive = eventActive;
    }

    public static long getLastEventTime() {
        return lastEventTime;
    }

    public static void setLastEventTime(long lastEventTime) {
        CacheEventController.lastEventTime = lastEventTime;
    }

    public static double getMultiplier() {
        return multiplier;
    }

    public static void setMultiplier(double multiplier) {
        CacheEventController.multiplier = multiplier;
    }

    public static boolean isStartup() {
        return startup;
    }

    public static void setStartup(boolean startup) {
        CacheEventController.startup = startup;
    }

    public static int getPrize() {
        return prize;
    }

    public static void setPrize(int prize) {
        CacheEventController.prize = prize;
    }

    public static void setTimeToEvent(int timeToEvent) {
        CacheEventController.timeToEvent = timeToEvent;
    }
}
