package remibot.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public final class DiscordUserAccess {

    private final MongoDBConnection connection;

    public DiscordUserAccess(final MongoDBConnection connection) {
        this.connection = connection;
    }

    public void createNew(final String discordId, final String runescapeUsername, final String accountType) {
        final DiscordUser newUser = new DiscordUser();
        newUser.setDiscordId(discordId);
        newUser.setRunescapeUsername(runescapeUsername);
        newUser.setAccountType(accountType);
        newUser.setSlayerPoints(0);
        newUser.setSlayerTask(null);
        insertOne(newUser);
    }

    @Nullable
    public DiscordUser findByDiscordId(final String discordId) {
        return getUsers().find(eq("discordId", discordId)).first();
    }

    @Nullable
    public DiscordUser findByRunescapeUsername(final String runescapeUsername) {
       return getUsers().find(eq("runescapeUsername", runescapeUsername)).first();
    }

    @Nullable
    public DiscordUser findByAccountType(final String accountType ) {
        return getUsers().find(eq("accountType", accountType)).first();
    }

    public InsertOneResult insertOne(final DiscordUser discordUser) {
        return getUsers().insertOne(discordUser);
    }

    public UpdateResult replaceOne(final DiscordUser discordUser) {
        final Bson filter = eq("discordId", discordUser.getDiscordId());
        final ReplaceOptions options = new ReplaceOptions().upsert(false);
        return getUsers().replaceOne(filter, discordUser, options);
    }

    private MongoCollection<DiscordUser> getUsers() {
        return connection.eventcord().getCollection("users", DiscordUser.class);
    }
}
