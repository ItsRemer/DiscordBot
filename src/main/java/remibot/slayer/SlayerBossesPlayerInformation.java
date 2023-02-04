package remibot.slayer;


import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class SlayerBossesPlayerInformation {

    private static HashMap<User, String> playerNames = new HashMap<>();


    public static void setPlayerName(User user, String name) {
        playerNames.put(user, name);
    }

    public static String getPlayerName(User user) {
        return playerNames.get(user);
    }

    /*public static int getKillCount(String user, String bossName) {
        // Use the playerName and bossName to look up the kill count in the database
        return SlayerBossTracker.getKillCount(user, bossName);
    }*/
}
