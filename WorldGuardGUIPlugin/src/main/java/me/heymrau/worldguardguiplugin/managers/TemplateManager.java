package me.heymrau.worldguardguiplugin.managers;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.Template;
import me.heymrau.worldguardguiplugin.utils.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TemplateManager {
    private final WorldGuardGUIPlugin plugin;

    @Getter
    private final List<Template> templatesList = new ArrayList<>();

    public void addTemplate(Template template) {
        final Yaml templates = plugin.getTemplates();
        templatesList.add(template);
        List<String> allowedFlags = template.getEnabledFlags().stream().map(Flag::getName).collect(Collectors.toList());
        List<String> deniedFlags = template.getDeniedFlags().stream().map(Flag::getName).collect(Collectors.toList());
        templates.set("templates." + template.getName() + ".allowed-flags", allowedFlags);
        templates.set("templates." + template.getName() + ".denied-flags", deniedFlags);
        templates.save();


    }

    public void initializeTemplates() {
        plugin.getTemplates().getConfigurationSection("templates").getKeys(false).forEach(template -> {
            List<StateFlag> enabledFlags = getFlags(template, "allowed-flags");
            List<StateFlag> deniedFlags = getFlags(template, "denied-flags");
            Template templateModel = new Template(template, enabledFlags, deniedFlags);
            templatesList.add(templateModel);
        });

    }

    public List<StateFlag> getFlags(String templatePath, String flagPath) {
        List<StateFlag> flags = new ArrayList<>();
        plugin.getTemplates().getStringList("templates." + templatePath + "." + flagPath).forEach(flag -> flags.add(plugin.getWorldGuard().getFlagByName(flag)));
        return flags;
    }

}
