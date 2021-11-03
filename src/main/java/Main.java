import Admin.TestCommands;
import CatGoddess.Pray;
import Currency.*;
import Currency.Events.CacheEventMisc.CacheEventController;
import Currency.Events.EventManager;
import Currency.Games.CurrencyMine;
import Currency.Games.DiceGame;
import Currency.Games.PitGame;
import Currency.Games.VtuberStatCheck;
import Util.MongoHandler;
import Util.StartupHandler;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import okhttp3.EventListener;

import javax.security.auth.login.LoginException;

public class Main extends EventListener {

    public static void main(String[] args) throws LoginException, InterruptedException {

        ConnectionString connectionString = new ConnectionString("NA");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("Shicoin");

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        AudioSourceManagers.registerRemoteSources(playerManager);

        AudioPlayer player = playerManager.createPlayer();

        MongoHandler handler = new MongoHandler(database);
        CommandClientBuilder clientBuilder = new CommandClientBuilder();

        String token = "NA";

        clientBuilder.useDefaultGame();

        clientBuilder.setOwnerId("NA");

        EventWaiter waiter = new EventWaiter();

        clientBuilder.setPrefix("s!");
        clientBuilder.setAlternativePrefix("shido!");

        clientBuilder.addCommands(
                new ViewCurrency(),
                new EventManager(),
                new CacheEventController(),
                new ClaimCurrency(),
                new DiceGame(),
                new LeaderboardCurrency(),
                new CurrencyMine(),
                new PitGame(waiter),
                new Pray(),
                new TestCommands(),
                new VtuberStatCheck());

        clientBuilder.useHelpBuilder(true);

        JDA jda = JDABuilder.createDefault(token).setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("Turning on..."))
                .addEventListeners(waiter, clientBuilder.build())
                .addEventListeners(new Startup(),
                        new StartupHandler())
                .build();

        jda.awaitReady();

    }

}
