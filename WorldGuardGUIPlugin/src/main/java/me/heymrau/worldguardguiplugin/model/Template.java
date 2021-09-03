package me.heymrau.worldguardguiplugin.model;

import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Template {
    private final String name;
    private final List<StateFlag> enabledFlags;
    private final List<StateFlag> deniedFlags;
}
