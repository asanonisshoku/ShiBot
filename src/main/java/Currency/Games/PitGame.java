package Currency.Games;

//Bet currency with 1/5 odds with set bids 10, 100, 1000, 10000 3 * payout
//Themed around a pit

//gladiator duels theme in a pit

import Util.BotUser;
import Util.Helper;
import Util.MongoHandler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PitGame extends Command {
    Random random = new Random();

    private final EventWaiter waiter;
    //Base winning on stats
    //Strength, Speed, Defense
    String[] actions = {"hit", "punched", "blasted", "removed", "annihilated"};

    public PitGame(EventWaiter waiter) {
        this.waiter = waiter;
        this.name = "pit";
        this.aliases = new String[]{"gladiator", "bettingpool"};
        this.arguments = "<amount>";
        this.cooldown = 240; // 4 minutes
        this.help = "bet on who is going to win a gladiator duel";
    }

    //Call gladiators to pit
    //Let player bet on 1 vtuber
    //Winnings

    //A command to run a Gladiator Fight type game for users to watch and bet a set amount of currency on, a alternative way to use currency and have a little bit of entertainment
    @Override
    protected void execute(CommandEvent event) {
        String commandFailureMessage = "Please choose an amount to bet, 10, 100, 1000, 10000 to bet";

            try {
                //Grab 2 random vtubers from the database
                VtuberGladiator gladiatorOne = MongoHandler.getRandomVtuberGladiator();
                VtuberGladiator gladiatorTwo = MongoHandler.getRandomVtuberGladiator();

                Helper.respondToMessage("The Gladiator Vtuber's " + gladiatorOne.getName() + " and " + gladiatorTwo.getName() + " are gathering in the pit for a fight, Choose your fighter and place your bet you can bet 10, 100, 1000, 10000", event);
                Helper.respondToMessage("Use <amount> <name of fighter>", event);

                //Wait for a user to respond to the message posted in the discord channel with their bet and which combatant they want to bet on winning
                waiter.waitForEvent(MessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()), event1 -> {
                    try {
                        int bet = 0;
                        int playerSelectedGladiator = 0;

                        String[] arguments = event1.getMessage().getContentRaw().split("\\s+");

                        if (arguments.length < 2){
                            return;
                        }

                        try {
                            if (arguments[2] != null) {
                                arguments[1] = arguments[1] + " " + arguments[2];
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                        if (!arguments[1].equalsIgnoreCase(gladiatorOne.getName())){
                            if (!arguments[1].equalsIgnoreCase(gladiatorTwo.getName())){
                                return;
                            } else {
                                playerSelectedGladiator = 1;
                            }
                        }

                        //Limit the player bet to the intended values
                        switch (Integer.parseInt(arguments[0])) {
                            case 10:
                                bet = 10;
                                break;
                            case 100:
                                bet = 100;
                                break;
                            case 1000:
                                bet = 1000;
                                break;
                            case 10000:
                                bet = 10000;
                                break;
                            case 100000:
                                bet = 100000;
                                break;
                            default:
                                event1.getMessage().reply(commandFailureMessage).queue();
                                return;
                        }

                        //Get user and check bet is valid

                        BotUser botUser = MongoHandler.getUser(event1.getMessage().getAuthor().getId());

                        if (!botUser.hasEnoughCurrency() && botUser.getCurrency() < bet){
                            event1.getChannel().sendMessage("You do not have enough Shicoins to bet that amount please try again.").queue();
                            return;
                        }

                        botUser.removeCurrency(bet);

                        String winnerName = "";
                        boolean playerWin = false;

                        String playerWinMessage = botUser.getEffectiveName() + " has won " + (bet * 3) + " shicoins";

                        //Run match logic and pay out winning bet if the players guess wins
                        //If returns true vtuber 1 wins the fight

                        if (gladiatorStatResolver(gladiatorOne, gladiatorTwo, event1)){
                            winnerName = gladiatorOne.getName();
                            gladiatorOne.setTotalWins(gladiatorOne.getTotalWins() + 1);
                            if (playerSelectedGladiator == 0){
                                playerWin = true;
                                botUser.addCurrency(bet * 3);
                            }
                            MongoHandler.saveVtuberToDatabase(gladiatorOne);
                        } else {
                            winnerName = gladiatorTwo.getName();
                            gladiatorTwo.setTotalWins(gladiatorTwo.getTotalWins() + 1);
                            if (playerSelectedGladiator == 1){
                                playerWin = true;
                                botUser.addCurrency(bet * 3);
                            }
                            MongoHandler.saveVtuberToDatabase(gladiatorTwo);
                        }

                        event1.getChannel().sendMessage("And the winner of the match was " + winnerName).queue();

                        if (playerWin){
                            event1.getMessage().reply(playerWinMessage).queue();
                        }

                        MongoHandler.saveUserToDatabase(botUser);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }, 120, TimeUnit.SECONDS, () -> Helper.respondToMessage("Match was canceled to to inactivity from the host", event));

            } catch (Exception e){
                Helper.respondToMessage(commandFailureMessage, event);
            }
    }

    //Runs rudimentary stat check with some randomness added in on who wins the fight along with some flavor text for the user
    private boolean gladiatorStatResolver(VtuberGladiator gladiator, VtuberGladiator gladiator1, MessageReceivedEvent event){
        //Strength
        //Defence
        //Speed

        //Strength > Defence > Speed
        //A Dodge can happen

        //Attack vs defence
        if (random.nextInt(2) == 0){
            //vtuber 1 attacks first
            if (gladiator.getStrength() + random.nextInt(3) > gladiator1.getDefence() + random.nextInt(5)){
                event.getChannel().sendMessage(gladiator.getName() + " managed to make a lunge for " + gladiator1.getName()).queue();
                if (gladiator1.getSpeed() + random.nextInt(2) > gladiator.getSpeed() + random.nextInt(3)){
                    event.getChannel().sendMessage(gladiator1.getName() + " managed to dodged last second and " + actions[random.nextInt(5)] + " on " + gladiator.getName()).queue();
                    return false;
                }else {
                    event.getChannel().sendMessage(gladiator.getName() + " " + actions[random.nextInt(5)] + " " + gladiator1.getName() + " in a big flash, the crowd is roaring").queue();
                    return true;
                }
            } else {
                event.getChannel().sendMessage(gladiator.getName() + " missed " + gladiator1.getName() + " letting them have a moment of reversal").queue();
                if (gladiator1.getSpeed() + random.nextInt(4) > gladiator.getSpeed() + random.nextInt(2)){
                    event.getChannel().sendMessage(gladiator1.getName() + " managed to turn around and " + actions[random.nextInt(5)] + " " + gladiator.getName()).queue();
                    return false;
                }else {
                    event.getChannel().sendMessage(gladiator1.getName() + " failed to take the moment of action, " +gladiator.getName() + " took the opportunity and " + actions[random.nextInt(5)] + " " + gladiator1.getName() + " in a sudden game changer, the crowd is cheering at the show").queue();
                    return true;
                }
            }
        } else {
            //vtuber 2 attacks first
            if (gladiator1.getStrength() + random.nextInt(3) > gladiator.getDefence() + random.nextInt(5)){
                event.getChannel().sendMessage(gladiator1.getName() + " managed to side step and attack " + gladiator.getName()).queue();
                if (gladiator.getSpeed() + random.nextInt(2) > gladiator1.getSpeed() + random.nextInt(3)){
                    event.getChannel().sendMessage(gladiator.getName() + " managed to avoid it last second and " + actions[random.nextInt(5)] + "  " + gladiator1.getName() + "in annihilation").queue();
                    return true;
                }else {
                    event.getChannel().sendMessage(gladiator1.getName() + " " + actions[random.nextInt(5)] + " " + gladiator.getName() + " in a moment of skilled fighting, the crowd is roaring").queue();
                    return false;
                }
            } else {
                event.getChannel().sendMessage(gladiator1.getName() + " missed " + gladiator.getName() + " letting them have a moment of to make a reversal").queue();
                if (gladiator.getSpeed() + random.nextInt(4) > gladiator1.getSpeed() + random.nextInt(2)){
                    event.getChannel().sendMessage(gladiator.getName() + " managed to use this opportunity and " + actions[random.nextInt(5)] + " " + gladiator1.getName()).queue();
                    return true;
                }else {
                    event.getChannel().sendMessage(gladiator.getName() + " failed to take the moment of action, " +gladiator1.getName() + " took the moment and " + actions[random.nextInt(5)] + " " + gladiator.getName() + " in a final blow, the crowd is loving it").queue();
                    return false;
                }
            }
        }
    }
}
