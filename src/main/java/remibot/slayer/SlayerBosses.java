package remibot.slayer;

public record SlayerBosses(String name, String weakness, int slayerLevel) {

    public String getName() {
        return name;
    }

    public String getWeakness() {
        return weakness;
    }

    public int getSlayerLevel() {
        return slayerLevel;
    }
}
