package me.heymrau.worldguardguiplugin.prompt;

import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

@RequiredArgsConstructor
public class RegionNamePrompt extends StringPrompt {

    private final WorldGuardService worldGuardService;

    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "Type a new name for the region named " + context.getSessionData("regionName");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        worldGuardService.rename(context.getSessionData("regionName").toString(), input);
        context.getForWhom().sendRawMessage(ChatColor.GREEN + "Region name is changed to " + input);
        return Prompt.END_OF_CONVERSATION;
    }
}
