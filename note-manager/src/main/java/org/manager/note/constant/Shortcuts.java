package org.manager.note.constant;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import lombok.Getter;

public enum Shortcuts {
    FORMAT(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)),
    SAVE(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

    @Getter
    private final KeyCombination shortcut;

    Shortcuts(KeyCombination shortcut) {
        this.shortcut = shortcut;
    }
}
