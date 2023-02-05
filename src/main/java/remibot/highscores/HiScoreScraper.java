package remibot.highscores;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HiScoreScraper {
    public Map<String, KillCountEntry> scrapeKillCounts(final AccountType accountType, final String username) {
        try {
            final Map<String, KillCountEntry> rankings = new HashMap<>();
            final Document document = Jsoup.connect(getHiscoreUrl(accountType, username)).get();
            final List<Element> killCounts = getKillCounts(document);

            for (int i = 0; i < killCounts.size(); i++) {
                final Element root = killCounts.get(i);
                final Element trackableLink = root.select("td a").first();
                final String trackableName = trackableLink.text();
                final Elements values = root.select("td[align=right]").not("td:has(img)");

                final int rank = Integer.parseInt(values.get(0).text().replace(",", ""));
                final int score = Integer.parseInt(values.get(1).text().replace(",", ""));

                rankings.put(trackableName.toLowerCase(), new KillCountEntry(rank, score));
            }
            return rankings;
        } catch (final IOException e) {
            throw new HiScoreException(e);
        }
    }

    private List<Element> getKillCounts(Document webpage) {
        List<Element> killCountRows = webpage.getAllElements().stream().filter(e -> e.tagName().equals("tr") && e.childrenSize() == 4).collect(Collectors.toList());
        killCountRows.remove(0);
        return killCountRows;
    }

    private List<Element> getSkillRows(Document webpage) {
        List<Element> skillRows = webpage.getAllElements().stream().filter(e -> e.tagName().equals("tr") && e.childrenSize() == 5).collect(Collectors.toList());
        skillRows.remove(0);
        return skillRows;
    }

    public enum AccountType {
        N("hiscore_oldschool"),
        IM("hiscore_oldschool_ironman"),
        UIM("hiscore_oldschool_ultimate"),
        HCIM("hiscore_oldschool_hardcore_ironman");

        private final String urlSuffix;

        AccountType(String urlSuffix) {
            this.urlSuffix = urlSuffix;
        }

        public String getUrlSuffix() {
            return urlSuffix;
        }
    }

    private String getHiscoreUrl(final AccountType accountType, final String username) {
        return "https://secure.runescape.com/m=" + accountType.getUrlSuffix() + "/hiscorepersonal?user1=" + username;
    }


}
