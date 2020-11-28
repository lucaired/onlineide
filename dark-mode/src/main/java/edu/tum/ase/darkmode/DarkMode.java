package edu.tum.ase.darkmode;

import java.time.LocalDateTime;

public class DarkMode {

    private boolean enabled;

    // TODO: This can probably be set automatically by the database when changes are made (exercise sheet 4)
    private LocalDateTime updated;

    public DarkMode() {
        // By default the dark mode is not activated
        setEnabled(false);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.updated = LocalDateTime.now();
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void toggle() {
        setEnabled(!enabled);
    }
}
