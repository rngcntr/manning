package model;

import org.json.simple.*;

public class Game {

    private long homeScore = 0;
    private long guestScore = 0;

    private String homeShort = "";
    private String guestShort = "";

    private long id = 0L;
    private String state = "";

    private boolean redzone;

    public static Game fromJSON (long id, JSONObject jsonObject) {
        Game output = new Game();

        try {
            output.id = id;
            output.state = (String) jsonObject.get("qtr");

            output.homeScore = (long) ((JSONObject) ((JSONObject) jsonObject.get("home")).get("score")).get("T");
            output.guestScore = (long) ((JSONObject) ((JSONObject) jsonObject.get("away")).get("score")).get("T");

            output.homeShort = (String) ((JSONObject) jsonObject.get("home")).get("abbr");
            output.guestShort = (String) ((JSONObject) jsonObject.get("away")).get("abbr");

            output.redzone = (boolean) jsonObject.get("redzone");
            //  "gsis": 56933,
            //  "rz": -1,
        } catch (NullPointerException npex) {
            System.err.println("Unable to parse Game from JSON");
            return null;
        }

        return output; 
    }

    public long getID () {
        return id;
    }

    public String getHome () {
        return homeShort;
    }

    public String getGuest () {
        return guestShort;
    }

    public boolean isRunning () {
        boolean running = false;

        switch (state) {
            case "1":
            case "2":
            case "H":
            case "3":
            case "4":
            case "5":
                running = true;
                break;
            default:
                running = false;
                break;
        }

        return running;
    }

    public String toString () {
        StringBuilder scoreBox = new StringBuilder();
        if(isRunning()) {
            scoreBox.append("╔═════╤═══════════╤═════╗\n");
            scoreBox.append(String.format("║ %3d │ %3s @ %-3s │ %-3d ║\n",
                        guestScore, guestShort, homeShort, homeScore));
            scoreBox.append("╚═════╧═══════════╧═════╝\n");
        } else {
            scoreBox.append("┌─────┬───────────┬─────┐\n");
            scoreBox.append(String.format("│ %3d │ %3s @ %-3s │ %-3d │\n",
                        guestScore, guestShort, homeShort, homeScore));
            scoreBox.append("└─────┴───────────┴─────┘\n");
        }

        return Printer.decorate(scoreBox.toString(), generateModifiers());
    }

    private String generateModifiers () {
        StringBuilder modifiers = new StringBuilder();

        if (redzone) {
            modifiers.append(Printer.ANSI_RED_BOLD);
        } else if (isRunning()) {
            modifiers.append(Printer.ANSI_WHITE_BOLD);
        }

        return modifiers.toString();
    }

}
