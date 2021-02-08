package edu.tum.ase.darkmode;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/dark-mode")
public class DarkModeController {

    /**
     * Number of seconds that must pass after switching the mode before the next toggle.
     * This is to prevent flickering between light and dark mode.
     */
    private static final int COOL_DOWN_TIME = 3;

    private final DarkMode darkMode = new DarkMode();

    @RequestMapping(path = "/toggle", method = RequestMethod.GET)
    public DarkMode toggleDarkMode() {
        long difference = ChronoUnit.SECONDS.between(darkMode.getUpdated(), LocalDateTime.now());
        if (difference > COOL_DOWN_TIME) {
            darkMode.toggle();
        }
        // TODO: Maybe we should include some information in the response if the cool-down time has not passed yet

        // Acknowledge the possibly updated status
        return darkMode;
    }

    @RequestMapping(method = RequestMethod.GET)
    public DarkMode getModeStatus() {
        return darkMode;
    }

}