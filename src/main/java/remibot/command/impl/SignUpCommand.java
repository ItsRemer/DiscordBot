package remibot.command.impl;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.command.Command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SignUpCommand implements Command {
    private static final Map<String, Message> buyInRequests = new ConcurrentHashMap<>();
    private static final Map<String, Integer> buyInAmounts = Collections.synchronizedMap(new HashMap<>());

    private static final String BUY_IN_COMMAND = "!buyin";
    private static final String PAID_COMMAND = "!paid";
    private static final String STAFF_ROLE_NAME = "Staff";
    private static final String REMI_BOT_STAFF_ID = "1065793686035759104";
    private static final String STAFF_ROLE_ID = "958489978835456091";

    // The handle method is the main method of the class, which implements the Command interface
    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        // Get the member who sent the message
        Member member = event.getMember();
        // Return if the member is null (if the member is not in the server)
        if (member == null) {
            return;
        }

        // Get the "sign-ups-register" and "sign-ups" text channels
        TextChannel buyInChannel = getTextChannel(event.getGuild(), "sign-ups-register");
        TextChannel paidChannel = getTextChannel(event.getGuild(), "sign-ups");

        // Check if the message starts with the BUY_IN_COMMAND
        if (event.getMessage().getContentRaw().startsWith(BUY_IN_COMMAND)) {
            handleBuyIn(event, buyInChannel);
        }
        // Check if the message starts with the PAID_COMMAND
        else if (event.getMessage().getContentRaw().startsWith(PAID_COMMAND)) {
            handlePaid(event.getGuild(), member, event.getMessage(), paidChannel, buyInChannel);
        }
    }

    private void handleBuyIn(MessageReceivedEvent event, TextChannel buyInChannel) {
        if (buyInRequests.containsKey(event.getAuthor().getId())) {
            event.getMessage().getChannel().sendMessage(event.getAuthor().getAsMention() + " you have already made a buy in request.").queue();
            return;
        }
        if (buyInChannel != null) {
            if (event.getMessage().getContentRaw().split(" ").length < 2) {
                event.getMessage().getChannel().sendMessage("Please make sure you are using the correct format! example; <!buyin 10M>").queue();
                return;
            }

            String amountString = event.getMessage().getContentRaw().split(" ")[1].toUpperCase();
            int amount;
            try {
                amount = Integer.parseInt(amountString.substring(0, amountString.length() - 1));
            } catch (NumberFormatException e) {
                event.getMessage().getChannel().sendMessage("Please use a valid number in your buy in request, for example; !buyin <10M>").queue();
                return;
            }

            if (!amountString.endsWith("M")) {
                event.getChannel().sendMessage("Please use the correct format, for example; !buyin <10M>").queue();
                return;
            }

            Role staffRole = event.getGuild().getRoleById(STAFF_ROLE_ID);
            Message buyInMessage = buyInChannel.sendMessage("<@&" + staffRole.getId() + "> " + event.getAuthor().getAsMention() + " would like to buy in for " + amount + "M gp.").complete();
            buyInRequests.put(event.getAuthor().getId(), buyInMessage);
            buyInAmounts.put(event.getAuthor().getId(), amount);
            event.getMessage().delete().queue();
        }
    }

    private void handlePaid(Guild guild, Member member, Message message, TextChannel paidChannel, TextChannel buyInChannel) {
        // Get the staff role
        Role staffRole = guild.getRolesByName(STAFF_ROLE_NAME, true).get(0);

        System.out.println(buyInAmounts);

        // Check if the author has the staff role
        if (!member.getRoles().contains(staffRole)) {
            return;
        }

        // Get the mentioned user
        List<User> mentionedUsers = message.getMentions().getUsers();
        if (mentionedUsers.isEmpty()) {
            return;
        }

        // Check if the channel exists
        if (paidChannel != null) {
            processPaidCommand(message, paidChannel, buyInChannel, mentionedUsers.get(0), member, staffRole);
        }
    }

    private void processPaidCommand(Message message, TextChannel paidChannel, TextChannel buyInChannel, User paidUser, Member member, Role staffRole) {
        // Send the paid message in the channel
        // Get the original buy-in message in the sign-ups-register channel
        Role staffRoleId = message.getGuild().getRoleById(STAFF_ROLE_ID);
        message.delete().queue();

        if (buyInAmounts.containsKey(paidUser.getId())) {
            long amount = buyInAmounts.get(paidUser.getId());
            List<Message> buyInMessages = buyInChannel != null ? buyInChannel.getHistory().retrievePast(100).complete().stream()
                    .filter(m -> m.getContentRaw().contains(paidUser.getAsMention() + " would like to buy in for " + amount + "M gp."))
                    .filter(m -> m.getAuthor().isBot() && member.getRoles().contains(staffRoleId)).toList() : null;

            // Delete the original buy-in message
            if (buyInMessages != null && !buyInMessages.isEmpty()) {
                Message buyInMessage = buyInMessages.get(0);
                buyInMessage.delete().queue();
                paidChannel.sendMessage(paidUser.getAsMention() + " has paid the " + amount + "M gp buy-in to " + message.getAuthor().getAsMention()).complete();
            }
        } else {
            paidChannel.sendMessage("There seems to be an error with trying to buy-in").queue();
        }
    }

    private TextChannel getTextChannel(Guild guild, String channelName) {
        List<TextChannel> channels = guild.getTextChannelsByName(channelName, true);
        if (channels.isEmpty()) {
            return null;
        } else {
            return channels.get(0);
        }
    }
}