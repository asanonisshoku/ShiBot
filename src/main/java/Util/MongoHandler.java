package Util;

import Currency.Games.VtuberGladiator;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.*;

public class MongoHandler {
    //Will handle putting all currency and user information into the Database for future use
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> misc;
    private static MongoCollection<Document> vtubers;

    public MongoHandler(MongoDatabase inputDatabase) {
        database = inputDatabase;
        collection = database.getCollection("users");
        misc = database.getCollection("serverMisc");
        vtubers = database.getCollection("vtubers");
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(MongoDatabase database) {
        MongoHandler.database = database;
    }

    public static MongoCollection<Document> getCollection() {
        return collection;
    }

    public static void setCollection(MongoCollection<Document> collection) {
        MongoHandler.collection = collection;
    }


    /**
     * Tries to get a user object from the database if there is no user it returns a brand new blank user with the same id and continues as normal
     * @param userID
     * @return BotUser a internal bot user for usage in the program.
     */
    public static BotUser getUser(String userID){
        Document document = null;
        try {
            document = collection.find(eq("_id", userID)).first();
        } catch (Exception e){
            System.out.println(e);
        }

        BotUser botUser;
        if (document == null){
            botUser = new BotUser(userID, 100, 0, "0");
        }else {
            botUser = new BotUser(document.getString("_id"), document.getInteger("currency"), document.getLong("lastClaim"), document.getString("server_ID"));
            if (document.getBoolean("isAdmin") != null) {
                botUser.setAdmin(document.getBoolean("isAdmin"));
            }
            if (document.getString("effectiveName") != null) {
                botUser.setEffectiveName(document.getString("effectiveName"));
            }
            if (document.getString("karma") != null) {
                botUser.setKarma(document.getDouble("karma"));
            }
        }
        return botUser;
    }

    public static void saveServerInfoToDatabase(ServerInfo serverInfo){
        try {
            Document userDoc = new Document()
                    .append("_ServerID", serverInfo.getServerID());


            Bson updates = Updates.combine(
                    Updates.set("eventChannelID", serverInfo.getEventChannelID()),
                    Updates.set("catGodCoins", serverInfo.getCatGodCoins()),
                    Updates.set("catGodTotalCoins", serverInfo.getCatGodTotalCoins()),
                    Updates.set("currencyName", serverInfo.getCurrencyName())
            );

            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = misc.updateOne(userDoc, updates, options);
            } catch (MongoException e){
                System.out.println(e);
            }

        }
         catch (MongoException e){
                System.err.println(e);
        }
    }

    public static ServerInfo getServerInfo(CommandEvent event){
        Document document = null;
        try {
            document = misc.find(eq("_ServerID", event.getGuild().getId())).first();
        } catch (Exception e){
            System.out.println(e);
        }

        ServerInfo serverInfo = new ServerInfo(event.getGuild().getId(), document.getString("eventChannelID"));
        serverInfo.setCatGodCoins(document.getInteger("catGodCoins"));
        serverInfo.setCatGodTotalCoins(document.getInteger("catGodTotalCoins"));
        serverInfo.setCurrencyName(document.getString("currencyName"));

        return serverInfo;
    }


    /**
     * Saves a user Object to the Database, tries to replace old user data first, if it can't, it creates a new entry into the database.
     * @param botUser the internal User Object to save.
     */
    public static void saveUserToDatabase(BotUser botUser){
        try {
            Document userDoc = new Document()
                    .append("_id", botUser.getId());


            Bson updates = Updates.combine(
                    Updates.set("currency", botUser.getCurrency()),
                    Updates.set("lastClaim", botUser.getLastClaim()),
                    Updates.set("server_ID", botUser.getServerID()),
                    Updates.set("isAdmin", botUser.isAdmin()),
                    Updates.set("effectiveName", botUser.getEffectiveName()),
                    Updates.set("karma", botUser.getKarma())

            );

            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collection.updateOne(userDoc, updates, options);
            } catch (MongoException e){
                System.out.println(e);
            }

        } catch (MongoException e){
            System.err.println(e);
        }
    }

    //Gets user's in current guild and returns the top 5 currency holders for said guild
    public static BotUser[] getTopUserFiveCurrency(ServerInfo serverInfo){
        BotUser[] array = new BotUser[5];

        MongoCursor<Document> document = collection.find(eq("server_ID", serverInfo.getServerID()))
                .sort(Sorts.descending("currency")).limit(5).iterator();

        int i = 0;
        for (MongoCursor<Document> it = document; it.hasNext(); ) {
            Document doc = it.next();
            BotUser botUser = getUser(doc.getString("_id"));
            array[i] = botUser;
            System.out.println(i);

            System.out.println(botUser.getId());
            i++;
        }
        return array;
    }

    public static VtuberGladiator getVtuber(String userID){
        Document document = null;
        try {
            document = vtubers.find(eq("_id", userID)).first();
        } catch (Exception e){
            System.out.println("No vtuber with set ID, Generating new entry");
        }

        VtuberGladiator vtuberGladiator;
        if (document == null){
            vtuberGladiator = new VtuberGladiator(userID);
            vtuberGladiator.setRandomStats();
        }else {
            vtuberGladiator = new VtuberGladiator(document.getString("_id"), document.getInteger("strength"), document.getInteger("speed"), document.getInteger("defence"), document.getInteger("wins"));
        }
        return vtuberGladiator;
    }


    public static void saveVtuberToDatabase(VtuberGladiator vtuberGladiator){
        try {
            Document userDoc = new Document()
                    .append("_id", vtuberGladiator.getName());


            Bson updates = Updates.combine(
                    Updates.set("strength", vtuberGladiator.getStrength()),
                    Updates.set("speed", vtuberGladiator.getSpeed()),
                    Updates.set("defence", vtuberGladiator.getDefence()),
                    Updates.set("wins", vtuberGladiator.getTotalWins())
            );

            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = vtubers.updateOne(userDoc, updates, options);
            } catch (MongoException e){
                System.out.println(e);
            }

        } catch (MongoException e){
            System.err.println(e);
        }
    }

    /**
     * Query's a Vtuber by name from the MongoDB without creating a new entry on non-usable query
     * @param vtuberName the full name of said Vtuber matching what is in the _id field of the database
     * @return returns Vtuber if the query name matches to the database, otherwise returns NULL
     */
    public static VtuberGladiator queryVtuberFromDataBase(String vtuberName){
        try {
            Document document = null;
            try {
                document = vtubers.find(eq("_id", vtuberName)).first();
            } catch (Exception e){
                System.out.println(e);
                System.out.println("No Vtuber found returning Null");
            }

            return new VtuberGladiator(document.getString("_id"), document.getInteger("strength"), document.getInteger("speed"), document.getInteger("defence"), document.getInteger("wins"));

        } catch (MongoException e){
            System.out.println(e);
        }

        return null;
    }

    public static VtuberGladiator getRandomVtuberGladiator(){
        AggregateIterable<Document> document = null;

        try {
           document =  vtubers.aggregate(Arrays.asList(Aggregates.sample(1)));
        } catch (Exception e){
            System.out.println(e);
        }

        VtuberGladiator gladiator = null;
        for (Document doc : document){
            gladiator = new VtuberGladiator(doc.getString("_id"), doc.getInteger("strength"), doc.getInteger("speed"), doc.getInteger("defence"), doc.getInteger("wins"));
        }

        return gladiator;
    }

    public static VtuberGladiator getRandomVtuberGladiator(VtuberGladiator previousGladiator){
        AggregateIterable<Document> document = null;

        try {
            document =  vtubers.aggregate(Arrays.asList(Aggregates.sample(1)));
        } catch (Exception e){
            System.out.println(e);
        }

        VtuberGladiator gladiator = null;
        for (Document doc : document){
            gladiator = new VtuberGladiator(doc.getString("_id"), doc.getInteger("strength"), doc.getInteger("speed"), doc.getInteger("defence"), doc.getInteger("wins"));
        }

        return gladiator;
    }




}
