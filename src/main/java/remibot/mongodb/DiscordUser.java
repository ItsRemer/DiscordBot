package remibot.mongodb;

public class DiscordUser {

    private String discordId;

    private String runescapeUsername;

    private String accountType;

    private int slayerPoints;

    private SlayerTask slayerTask;

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getRunescapeUsername() {
        return runescapeUsername;
    }

    public void setRunescapeUsername(String runescapeUsername) {
        this.runescapeUsername = runescapeUsername;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getSlayerPoints() {
        return slayerPoints;
    }

    public void setSlayerPoints(int slayerPoints) {
        this.slayerPoints = slayerPoints;
    }

    public SlayerTask getSlayerTask() {
        return slayerTask;
    }

    public void setSlayerTask(SlayerTask slayerTask) {
        this.slayerTask = slayerTask;
    }
}
