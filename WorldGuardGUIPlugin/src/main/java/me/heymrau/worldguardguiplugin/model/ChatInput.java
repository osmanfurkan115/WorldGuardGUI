package me.heymrau.worldguardguiplugin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatInput {
    private final String regionName;
    private final InputType inputType;
}
