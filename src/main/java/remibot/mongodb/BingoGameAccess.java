package remibot.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.lang.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;

public class BingoGameAccess {

    private final MongoDBConnection connection;

    public BingoGameAccess(final MongoDBConnection connection) {
        this.connection = connection;
    }

    public void createNew(final String guildId, final Map<String, List<BingoItem>> bingoItems) {
        softDelete(guildId);

        final BingoGame bingoGame = new BingoGame();
        bingoGame.setBingoItems(bingoItems);
        bingoGame.setGuildId(guildId);
        bingoGame.setId(UUID.randomUUID().toString());
        bingoGame.setActive(true);
        bingoGame.setTimestamp(Instant.now());

        insertOne(bingoGame);
    }

    public void softDelete(final String guildId) {
        final BingoGame latestGame = findLatestActiveByGuildId(guildId);
        if (latestGame != null) {
            updateActiveStatus(latestGame.getId(), false);
        }
    }

    public void updateActiveStatus(final String gameId, final boolean status) {
        getBingoGames().updateOne(
                eq("_id", gameId),
                set("active", status)
        );
    }

    @Nullable
    public List<BingoGame> findAllByGuildId(final String guildId) {
        return getBingoGames().find(eq("guildId", guildId)).into(new ArrayList<>());
    }

    @Nullable
    public List<BingoGame> findAllActiveByGuildId(final String guildId) {
        return getBingoGames().find(and(eq("guildId", guildId), eq("active", true))).into(new ArrayList<>());
    }

    @Nullable
    public BingoGame findLatestActiveByGuildId(final String guildId) {
        final FindIterable<BingoGame> result = getBingoGames()
                .find(and(eq("guildId", guildId), eq("active", true)))
                .sort(descending("timestamp"))
                .limit(1);

        return result.first();
    }

    public InsertOneResult insertOne(final BingoGame bingoGame) {
        return getBingoGames().insertOne(bingoGame);
    }

    private MongoCollection<BingoGame> getBingoGames() {
        return connection.eventcord().getCollection("bingo_games", BingoGame.class);
    }
}