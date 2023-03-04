package org.manager.note.constant;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public enum Shortcuts {
    FORMAT(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

    private final KeyCombination shortcut;

    Shortcuts(KeyCombination shortcut) {
        this.shortcut = shortcut;
    }

    public KeyCombination getShortcut() {
        return shortcut;
    }
}
