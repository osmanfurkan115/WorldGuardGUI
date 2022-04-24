package me.heymrau.worldguardguiplugin.model;

import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Template {
    private final String name;
    private final List<StateFlag> enabledFlags;
    private final List<StateFlag> deniedFlags;
}
