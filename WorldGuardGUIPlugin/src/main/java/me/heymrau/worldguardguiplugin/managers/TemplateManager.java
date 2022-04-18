package me.heymrau.worldguardguiplugin.managers;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.Template;
import me.heymrau.worldguardguiplugin.utils.Yaml;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TemplateManager {
    private final WorldGuardGUIPlugin plugin;

    @Getter
    private final Set<Template> templatesList = new HashSet<>();

    public void addTemplate(Template template) {
        Yaml templates = plugin.getTemplates();

        templates.set("templates." + template.getName() + ".allowed-flags", template.getEnabledFlags().stream().map(Flag::getName).collect(Collectors.toList()));
        templates.set("templates." + template.getName() + ".denied-flags", template.getDeniedFlags().stream().map(Flag::getName).collect(Collectors.toList()));
        templates.save();

        Optional<Template> optionalTemplate = templatesList.stream()
                .filter(otherTemplate -> otherTemplate.getName().equalsIgnoreCase(template.getName()))
                .findFirst();
        optionalTemplate.ifPresent(templatesList::remove);
        templatesList.add(template);
    }

    public void initializeTemplates() {
        plugin.getTemplates().getConfigurationSection("templates").getKeys(false).forEach(template -> {
            final Template templateModel = new Template(template, getFlags(template, "allowed-flags"), getFlags(template, "denied-flags"));
            templatesList.add(templateModel);
        });

    }

    public List<StateFlag> getFlags(String templatePath, String flagPath) {
        return plugin.getTemplates().getStringList("templates." + templatePath + "." + flagPath).stream()
                .map(plugin.getWorldGuard()::getFlagByName).collect(Collectors.toList());
    }

}
