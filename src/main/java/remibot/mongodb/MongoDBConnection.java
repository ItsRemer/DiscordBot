package remibot.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnection {

    private final MongoDatabase eventcord;

    private final DiscordUserAccess discordUserAccess;

    private final BingoGameAccess bingoGameAccess;

    public MongoDBConnection(final String connectionUrl) {
        final ConnectionString connectionString = new ConnectionString(connectionUrl);

        final CodecRegistry pojoRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .codecRegistry(fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoRegistry))
                .build();

        final MongoClient client = MongoClients.create(settings);
        this.eventcord = client.getDatabase("eventcord");

        this.discordUserAccess = new DiscordUserAccess(this);
        this.bingoGameAccess = new BingoGameAccess(this);
    }

    public DiscordUserAccess getDiscordUserAccess() {
        return discordUserAccess;
    }

    public BingoGameAccess getBingoGameAccess() {
        return bingoGameAccess;
    }

    public MongoDatabase eventcord() {
        return eventcord;
    }
}
