package edu.tum.ase.darkmode;

import edu.tum.ase.darkmode.model.DarkMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@RestController
public class DarkmodeApplication {

    /**
     * Number of seconds that must pass after switching the mode before the next toggle.
     * This is to prevent flickering between light and dark mode.
     */
    private static final int COOL_DOWN_TIME = 3;

    // TODO: Replace this with the Hibernate version of the class (exercise sheet 4)
    private DarkMode darkMode = new DarkMode();

    public static void main(String[] args) {
        SpringApplication.run(DarkmodeApplication.class, args);
    }

    @RequestMapping(path = "/dark-mode/toggle", method = RequestMethod.GET)
    public DarkMode toggleDarkMode() {
        long difference = ChronoUnit.SECONDS.between(darkMode.getUpdated(), LocalDateTime.now());
        if (difference > COOL_DOWN_TIME) {
            darkMode.toggle();
        }
        // TODO: Maybe we should include some information in the response if the cool-down time has not passed yet

        // Acknowledge the possibly updated status
        return darkMode;
    }

    @RequestMapping(path = "/dark-mode", method = RequestMethod.GET)
    public DarkMode getModeStatus() {
        return darkMode;
    }
}