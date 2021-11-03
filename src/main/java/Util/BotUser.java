package Util;

import com.jagrosh.jdautilities.command.CommandEvent;

public class BotUser {

    private String id;
    //How much of the chosen currency user has
    private int currency;
    //Last claim time in milliseconds
    private long lastClaim;
    private String serverID;
    private String effectiveName = "No Name Logged";
    private double karma = 1.0;

    //Is Admin of home Server
    private boolean isAdmin = false;

    public BotUser(String id, int currency, long lastClaim, String serverID) {
        this.id = id;
        this.currency = currency;
        this.lastClaim = lastClaim;
        this.serverID = serverID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public void addCurrency(int currency){
        this.currency += currency;
    }

    public void removeCurrency(int currency){
        this.currency -= currency;
    }

    public long getLastClaim() {
        return lastClaim;
    }

    public void setLastClaim(long lastClaim) {
        this.lastClaim = lastClaim;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public boolean hasEnoughCurrency(){
        return currency > 0;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getEffectiveName() {
        return effectiveName;
    }

    public void setEffectiveName(String effectiveName) {
        this.effectiveName = effectiveName;
    }

    public double getKarma() {
        return karma;
    }

    public void setKarma(double karma) {
        this.karma = karma;
    }
}
