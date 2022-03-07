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
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TemplateManager {
    private final WorldGuardGUIPlugin plugin;

    @Getter
    private final List<Template> templatesList = new ArrayList<>();

    public void addTemplate(Template template) {
        final Yaml templates = plugin.getTemplates();

        templates.set("templates." + template.getName() + ".allowed-flags", template.getEnabledFlags().stream().map(Flag::getName).collect(Collectors.toList()));
        templates.set("templates." + template.getName() + ".denied-flags", template.getDeniedFlags().stream().map(Flag::getName).collect(Collectors.toList()));
        templates.save();

        final Optional<Template> first = templatesList.stream().filter(template1 -> template1.getName().equals(template.getName())).findFirst();
        if (first.isPresent()) templatesList.set(templatesList.indexOf(first.get()), template);
        else templatesList.add(template);
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
