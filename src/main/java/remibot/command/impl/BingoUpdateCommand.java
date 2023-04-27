package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.mongodb.BingoItem;

import java.util.*;

public class BingoUpdateCommand implements Command {

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        if (args.length < 2) {
            event.getChannel().sendMessage("Usage: !set_bingo_items groupName;itemId1;itemId2...:groupName2;itemId3;itemId4...").queue();
            return;
        }

        String bingoItemsString = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String[] groups = bingoItemsString.split(":");

        Map<String, List<BingoItem>> bingoItemsMap = new LinkedHashMap<>();

        for (String group : groups) {
            String[] groupData = group.split(";");

            if (groupData.length > 1) {
                String groupName = groupData[0].trim();
                List<BingoItem> bingoItems = new ArrayList<>();

                for (int i = 1; i < groupData.length; i++) {
                    try {
                        int itemId = Integer.parseInt(groupData[i].trim());
                        BingoItem bingoItem = new BingoItem();
                        bingoItem.setItemId(itemId);
                        bingoItem.setAmount(1);
                        bingoItems.add(bingoItem);
                    } catch (NumberFormatException e) {
                        // Handle invalid item ID input
                    }
                }

                if (!bingoItems.isEmpty()) {
                    bingoItemsMap.put(groupName, bingoItems);
                }
            }
        }

        EventcordBot.mongoDB.getBingoGameAccess().createNew(event.getGuild().getId(), bingoItemsMap);
        event.getChannel().sendMessage("Bingo items updated!").queue();
    }
}