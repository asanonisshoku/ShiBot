package Util;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.WidgetUtil;

import javax.annotation.Nonnull;
import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Helper {
    public static Command.Category currency = new Command.Category("currency");
    public static Command.Category admin = new Command.Category("admin");
    public static Command.Category music = new Command.Category("music");

    //Identifier string that is called when someone wants to use the bot eg s!play Sandstorm
    private static String identifier = "s!";
    private static Member thisMember = null;

    public static boolean messageReceived(String string, MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().startsWith(identifier + string)){
            return true;
        }
        return false;
    }

    public static void respondToMessage(String text, CommandEvent commandEvent){
        commandEvent.getChannel().sendMessage(text).queue();
    }

    //Method to help send a message reducing lines of coded needed in Listener classes.
    public static void queueMessage(String string, MessageReceivedEvent event){
        event.getChannel().sendMessage(string).queue();
    }

    //Checks to see if it has been 24 hours since a users last claim.
    public static boolean canClaim(BotUser botUser){
        if (System.currentTimeMillis() >= botUser.getLastClaim() + 86400000){
            return true;
        }
        return false;
    }

    //Returns a string with how many minutes until a user can claim more currency
    public static String timeTillClaim(BotUser botUser){
        long millis = ((botUser.getLastClaim() + 86400000) - System.currentTimeMillis());

        return String.format("%d Hours, %d Minutes",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes((TimeUnit.MILLISECONDS.toHours(millis))
        ));
    }

    //Small helper to grab a user's id number from discord, usually for store and use with MongoDB.
    public static String getUserID(MessageReceivedEvent event){
        return event.getMessage().getAuthor().getId();
    }

    public static String getUserID(CommandEvent event){
        return event.getMessage().getAuthor().getId();
    }

    //Checks if a user has a server nickname and returns that, if there is none it uses a users default name.
    public static String getDefaultUserName(CommandEvent event, BotUser target){
        return event.getGuild().retrieveMember(event.getMessage().getMentionedUsers().get(0)).complete().getEffectiveName();
    }

    public static String getUserName(CommandEvent event, BotUser target, int index){
        return event.getGuild().retrieveMember(event.getMessage().getMentionedUsers().get(index)).complete().getEffectiveName();
    }

    public static String getUserName(CommandEvent event, String targetID, int index){
        return event.getGuild().retrieveMember(event.getMessage().getMentionedUsers().get(index)).complete().getEffectiveName();
    }

    public static boolean hasBotPerms(CommandEvent event,BotUser botUser){
        return event.getMember().hasPermission(Permission.ADMINISTRATOR) || botUser.isAdmin();
    }

    public static boolean isUser(CommandEvent event, int targetMention){
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
    }

    public static void setEffectiveNameToUser(CommandEvent event, BotUser botUser){
        botUser.setEffectiveName(event.getMessage().getMember().getEffectiveName());
    }
}
