package remibot.mongodb;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class BingoGame {
    private String id;
    private String guildId;
    private Map<String, List<BingoItem>> bingoItems;
    private boolean active;

    private Instant timestamp;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public Map<String, List<BingoItem>> getBingoItems() {
        return bingoItems;
    }

    public void setBingoItems(Map<String, List<BingoItem>> bingoItems) {
        this.bingoItems = bingoItems;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}