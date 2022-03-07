package me.heymrau.worldguardguiplugin.managers;

import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.ConversationType;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConversationManager {
    private final WorldGuardGUIPlugin plugin;

    public void beginBlockCommandConversation(Player player, String regionName) {
        final HashMap<Object, Object> dataMap = getMapByRegionName(regionName);
        beginConversation(player, ConversationType.BLOCK_COMMAND, dataMap);
    }

    public void beginRegionNameConversation(Player player, String regionName) {
        final HashMap<Object, Object> dataMap = getMapByRegionName(regionName);
        beginConversation(player, ConversationType.REGION_NAME, dataMap);
    }

    private HashMap<Object, Object> getMapByRegionName(String regionName) {
        final HashMap<Object, Object> dataMap = new HashMap<>();
        dataMap.put("regionName", regionName);
        return dataMap;
    }

    private void beginConversation(Player player, Prompt prompt, Map<Object, Object> initialSessionData) {
        getConversationFactory(prompt, initialSessionData).buildConversation(player).begin();
    }

    private ConversationFactory getConversationFactory(Prompt prompt) {
        return new ConversationFactory(plugin)
                .withLocalEcho(false)
                .withFirstPrompt(prompt)
                .withModality(true)
                .withTimeout(30)
                .withEscapeSequence("cancel");
    }

    private ConversationFactory getConversationFactory(Prompt prompt, Map<Object, Object> initialSessionData) {
        return getConversationFactory(prompt).withInitialSessionData(initialSessionData);
    }
}
