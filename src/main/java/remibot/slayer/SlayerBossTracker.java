package remibot.slayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class SlayerBossTracker {


    private static Map<String, String> fetchDataFromWebsite(String username) {
        String url = "https://secure.runescape.com/m=hiscore_oldschool/index_lite.ws?player=" + username;
        Map<String, String> dataMap = new HashMap<>();
        try {
            URL highscores = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(highscores.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] data = inputLine.split(",");
                dataMap.put(data[0], inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    public static Map<String, Integer> parseBosses(String username) {
        Map<String, Integer> subset = new HashMap<>();
        ArrayList<String> bosses = new ArrayList<>(Arrays.asList(
                "rifts_closed",
                "abyssal_sire",
                "alchemical_hydra",
                "barrows_chests",
                "bryophyta",
                "callisto",
                "cerberus",
                "chambers_of_xeric",
                "chambers_of_xeric_challenge",
                "chaos_elemental",
                "chaos_fanatic",
                "zulrah"
        ));

        Map<String, String> dataMap = fetchDataFromWebsite(username);

        // Iterate through the list of bosses
        for (String boss : bosses) {
            // Check if the dataMap contains the current boss
            if (dataMap.containsKey(boss)) {
                // Split the data and extract the kill count
                String[] data = dataMap.get(boss).split(",");
                int killCount = Integer.parseInt(data[2]);
                // Add the boss and its kill count to the subset map
                subset.put(boss, killCount);
            }
        }
        return subset;
    }
}