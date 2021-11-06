package Util;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.concurrent.TimeUnit;

public class Helper {

    //Categories to be used in a command for the help command
    public static Command.Category currency = new Command.Category("currency");
    public static Command.Category admin = new Command.Category("admin");
    public static Command.Category music = new Command.Category("music");

    //Identifier string that is called when someone wants to use the bot eg s!play Sandstorm

    private static String identifier = "s!";

/*    public static boolean messageReceived(String string, MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().startsWith(identifier + string)){
            return true;
        }
        return false;
    }*/

    public static void respondToMessage(String text, CommandEvent commandEvent){
        commandEvent.getChannel().sendMessage(text).queue();
    }

    /**
     * Checks to see if it has been 24 hours since a users last claim.
     * @param botUser target user
     * @return true if a user can claim a daily again
     */
    public static boolean canClaim(BotUser botUser){
        if (System.currentTimeMillis() >= botUser.getLastClaim() + 86400000){
            return true;
        }
        return false;
    }

    /**
     * A 24 hour converter method from milliseconds to Hours and Minutes eg, 7 Hours, 3 Minutes
     * @param botUser target user
     * @return String of formatted time till next claim
     */
    public static String timeTillClaim(BotUser botUser){
        long millis = ((botUser.getLastClaim() + 86400000) - System.currentTimeMillis());

        return String.format("%d Hours, %d Minutes",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes((TimeUnit.MILLISECONDS.toHours(millis))
        ));
    }

    //Small helper to grab a user's id number from discord, usually for store and use with MongoDB.

    /**
     * Helper method to quickly grab a users ID from a MessageReceivedEvent
     * @param event MessageReceivedEvent
     * @return String ID eg. 210778361880903680
     */
    public static String getUserID(MessageReceivedEvent event){
        return event.getMessage().getAuthor().getId();
    }

    /**
     * Helper method to quickly grab a users ID from a CommandEvent
     * @param event CommandEvent
     * @return String ID eg. 210778361880903680
     */
    public static String getUserID(CommandEvent event){
        return event.getMessage().getAuthor().getId();
    }

    /**
     * Returns a users server nickname, if there isn't one it returns the users default name.
     * Used mostly in cases where you only need to handle a single user.
     * @param event CommandEvent
     * @param target Target User
     * @return String Name of the User
     */
    public static String getDefaultUserName(CommandEvent event, BotUser target){
        return event.getGuild().retrieveMember(event.getMessage().getMentionedUsers().get(0)).complete().getEffectiveName();
    }

    /**
     * Target a specific user from a array of mentioned users, allowing use in loops and handling multiple mentioned users.
     * @param event CommandEvent
     * @param target target
     * @param index index of user in array
     * @return String Name of the User
     */
    public static String getUserName(CommandEvent event, BotUser target, int index){
        return event.getGuild().retrieveMember(event.getMessage().getMentionedUsers().get(index)).complete().getEffectiveName();
    }

    /**
     * Confirmes whether a user has server admin permissions or bot permissions given by an admin.
     * @param event CommandEvent
     * @param botUser Target User
     * @return True/False
     */
    public static boolean hasBotPerms(CommandEvent event,BotUser botUser){
        return event.getMember().hasPermission(Permission.ADMINISTRATOR) || botUser.isAdmin();
    }

/*    public static boolean isUser(CommandEvent event, int targetMention){
        User user = null;

        try {
            user = event.getMessage().getMentionedUsers().get(targetMention);
        } catch (Exception e){
            System.out.println("Not a user");
        }
        if (user != null){
            return true;
        }
        return false;
    }

    public static User getUserFromMentioned(CommandEvent event, int mentionedTarget){
        if (isUser(event, mentionedTarget)){
            return event.getMessage().getMentionedUsers().get(mentionedTarget);
        }
        return null;
    }*/

    /**
     * Saves the Effective name of a user to their botUser object which is saved on to the database.
     * The effective name tries for a User's nickname first before defaulting to user name.
     * @param event CommandEvent
     * @param botUser Target User
     */
    public static void setEffectiveNameToUser(CommandEvent event, BotUser botUser){
        botUser.setEffectiveName(event.getMessage().getMember().getEffectiveName());
    }
}
