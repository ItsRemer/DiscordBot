package remibot.slayer;

import java.util.ArrayList;

public class SlayerBossesDatabase {

    private final ArrayList<SlayerBosses> bosses;
    private static SlayerBossesDatabase instance = null;

    private SlayerBossesDatabase() {
        bosses = new ArrayList<>();
        bosses.add(new SlayerBosses("Kalphite Queen", "Magic", 0));
        bosses.add(new SlayerBosses("King Black Dragon", "Magic", 0));
        bosses.add(new SlayerBosses("zulrah", "Magic & Range", 0));
        bosses.add(new SlayerBosses("Vorkath", "Stab & Range", 0));
        bosses.add(new SlayerBosses("Alchemical Hydra", "Magic", 95));
        bosses.add(new SlayerBosses("Vet'ion", "Magic", 0));
        bosses.add(new SlayerBosses("Chaos Elemental", "Magic", 0));
        bosses.add(new SlayerBosses("Cerberus", "Magic", 91));
        bosses.add(new SlayerBosses("Thermonuclear Smoke Devil", "Magic", 93));
        bosses.add(new SlayerBosses("Corporeal Beast", "Magic", 0));
        bosses.add(new SlayerBosses("Giant Mole", "Magic", 0));
        bosses.add(new SlayerBosses("General Graardor", "Magic", 0));
        bosses.add(new SlayerBosses("Kree'Arra", "Magic", 0));
        bosses.add(new SlayerBosses("K'ril Tsutsaroth", "Magic", 0));
        bosses.add(new SlayerBosses("Commander Zilyana", "Magic", 0));
        bosses.add(new SlayerBosses("Chaos Fanatic", "Magic", 0));
        bosses.add(new SlayerBosses("Crazy Archaeologist", "Magic", 0));
        bosses.add(new SlayerBosses("Chaos Elemental", "Magic", 0));
        bosses.add(new SlayerBosses("Abyssal Sire", "Magic", 85));
        bosses.add(new SlayerBosses("Archaeologist", "Magic", 0));
        bosses.add(new SlayerBosses("Kraken", "Magic", 87));
    }

    public static SlayerBossesDatabase getInstance() {
        if (instance == null) {
            instance = new SlayerBossesDatabase();
        }
        return instance;
    }

    public ArrayList<SlayerBosses> getBosses() {
        return bosses;
    }

    public SlayerBosses getBoss(String name) {
        for (SlayerBosses b : bosses) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }
}
