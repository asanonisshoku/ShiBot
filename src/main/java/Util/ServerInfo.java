package Util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerInfo {

    //Internal server Object to save on database calls

    private String eventChannelID;
    private String ServerID;
    private int catGodCoins;
    private int catGodTotalCoins;

    private String currencyName = "shicoin";
    private String[] quickRequestSongs = {};

    public ServerInfo(String serverID, String eventChannelID) {
        this.ServerID = serverID;
        this.eventChannelID = eventChannelID;
    }

    public String getServerID() {
        return ServerID;
    }

    public void setServerID(String serverID) {
        ServerID = serverID;
    }

    public String getEventChannelID() {
        return eventChannelID;
    }

    public void setEventChannelID(String eventChannelID) {
        this.eventChannelID = eventChannelID;
    }

    public int getCatGodCoins() {
        return catGodCoins;
    }

    public void setCatGodCoins(int catGodCoins) {
        this.catGodCoins = catGodCoins;
    }

    public int getCatGodTotalCoins() {
        return catGodTotalCoins;
    }

    public void setCatGodTotalCoins(int catGodTotalCoins) {
        this.catGodTotalCoins = catGodTotalCoins;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
