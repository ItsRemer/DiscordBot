package remibot.slayer;

public enum Bosses {
    ABYSSAL_SIRE("Abyssal Sire", 10, 10),
    BARROWS("Barrows Chests", 10, 10),
    CERBERUS("Cerberus", 10, 10),
    ALCHEMICAL_HYDRA("Alchemical Hydra", 10, 10),
    CHAOS_ELEMENTAL("Chaos Elemental", 10, 10),
    CALLISTO("Callisto", 10, 10),
    CORPOREAL_BEAST("Corporeal Beast", 10, 10),
    KING_BLACK_DRAGON("King Black Dragon", 10, 10),
    KRAKEN("Kraken", 10, 10),
    COX("Chambers of Xeric", 10, 10),
    COX_HARD("Chambers of Xeric: Challenge Mode", 10, 10),
    CHAOS_FANATIC("Chaos Fanatic", 10, 10),
    COMMANDER_ZILYANA("Commander Zilyana", 10, 10),
    GENERAL_GRAARDOR("General Graardor", 10, 10),
    CRAZY_ARCH("Crazy Archaeologist", 10, 10),
    RIFTS_CLOSED("Rifts closed", 10, 10),
    PRIME("Dagannoth Prime", 10, 10),
    REX("Dagannoth Rex", 10, 10),
    SUPREME("Dagannoth Supreme", 10, 10),
    DERANGED_ARCH("Deranged Archaeologist", 10, 10),
    GIANT_MOLE("Giant Mole", 10, 10),
    GROTESQUE("Grotesque Guardians", 10, 10),
    KALPHITE_QUEEN("Kalphite Queen", 10, 10),
    KREE_ARA("Kree'Arra", 10, 10),
    KRIL("K'ril Tsutsaroth", 10, 10),
    NEX("Nex", 10, 10),
    NIGHTMARE("Nightmare", 10, 10),
    PHO_NIGHTMARE("Phosani's Nightmare", 10, 10),
    MUSPAH("Phantom Muspah", 10, 10),
    SARACHNIS("Sarachnis", 10, 10),
    SCORPIA("Scorpia", 10, 10),
    TEMPOROSS("Tempoross", 10, 10),
    CORRUPT_GAUNTLET("The Corrupted Gauntlet", 10, 10),
    TOB("Theatre of Blood", 10, 10),
    TOB_HARD("Theatre of Blood: Hard Mode", 10, 10),
    THERMO_SMOKE_DEVIL("Thermonuclear Smoke Devil", 10, 10),
    TOA("Tombs of Amascut", 10, 10),
    TOA_EXPERT("Tombs of Amascut: Expert Mode", 10, 10),
    ZUK("TzKal-Zuk", 10, 10),
    JAD("TzTok-Jad", 10, 10),
    VENENATIS("Venenatis", 10, 10),
    VETION("Vet'ion", 10, 10),
    VORKATH("Vorkath", 10, 10),
    WINTERTODT("Wintertodt", 10, 10),
    ZALCANO("Zalcano", 10, 10),
    ZULRAH("Zulrah", 10, 10);

    private final String name;
    private final int minAmount;
    private final int maxAmount;

    Bosses(final String name, final int minAmount, final int maxAmount) {
        this.name = name;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public String getName() {
        return name;
    }
}
