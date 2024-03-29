package me.heymrau.worldguardguiplugin.prompt;

import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

@RequiredArgsConstructor
public class BlockCommandPrompt extends StringPrompt {
    private final WorldGuardService worldGuardService;

    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "Type a command to block for the region named " + context.getSessionData("regionName") + " in 30 seconds";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        worldGuardService.addBlockedCommand(worldGuardService.getRegionByName(context.getSessionData("regionName").toString()),
                input);
        context.getForWhom().sendRawMessage(ChatColor.GREEN + "Command " + input + " is added to the blocked commands");
        return Prompt.END_OF_CONVERSATION;
    }
}
