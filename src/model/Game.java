package model;

import org.json.simple.*;

public class Game {

	private long homeScore = -1L;
	private long guestScore = -1L;
	
	private String homeShort = "";
	private String guestShort = "";

    private long homeTimeouts = 3L;
    private long guestTimeouts = 3L;

    private long down = 0L;
    private long togo = 0L;
    private long quarter = 0L;
    private String yardline = "";
    private String clock = "";

    private String posTeam = "";

	private long id = 0L;
	private String state = "Pre";

	private boolean redzone = false;

	public static Game fromJSON (long id, JSONObject jsonObject) {
		Game output = new Game();

		try {
			output.id = id;

            Object stateObject = jsonObject.get("qtr");
            if (stateObject != null) {
                output.state = (String) stateObject;
            }

            Object posTeamObject = jsonObject.get("posteam");
            if (posTeamObject != null) {
                output.posTeam = (String) posTeamObject;
            }

			output.homeShort = (String) ((JSONObject) jsonObject.get("home")).get("abbr");
			output.guestShort = (String) ((JSONObject) jsonObject.get("away")).get("abbr");

            Object homeScoreObject = ((JSONObject) ((JSONObject) jsonObject.get("home")).get("score")).get("T");
            if (homeScoreObject != null) {
                output.homeScore = (long) homeScoreObject;
            }

            Object guestScoreObject = ((JSONObject) ((JSONObject) jsonObject.get("away")).get("score")).get("T");
            if (guestScoreObject != null) {
                output.guestScore = (long) guestScoreObject;
            }

            Object homeTimeoutsObject = ((JSONObject) jsonObject.get("away")).get("to");
            if (homeTimeoutsObject != null) {
                output.homeTimeouts = (long) homeTimeoutsObject;
            }

            Object guestTimeoutsObject = ((JSONObject) jsonObject.get("away")).get("to");
            if (guestTimeoutsObject != null) {
                output.guestTimeouts = (long) guestTimeoutsObject;
            }

            Object downObject = jsonObject.get("down");
            if (downObject != null) {
                output.down = (long) downObject;;
            }

            Object togoObject = jsonObject.get("togo");
            if (togoObject != null) {
                output.togo = (long) togoObject;
            }

            Object quarterObject = jsonObject.get("quarter");
            if (quarterObject != null) {
                output.quarter = (long) quarterObject;
            }

            Object yardlineObject = jsonObject.get("yl");
            Object noteObject = jsonObject.get("note");
            if (yardlineObject != null) {
                output.yardline = (String) yardlineObject;
                if (noteObject != null) {
                    output.yardline = (String) noteObject;
                }
            }

            Object clockObject = jsonObject.get("clock");
            if (clockObject != null) {
                output.clock = (String) clockObject;
            }
			
			
            Object redzoneObject = jsonObject.get("redzone");
			output.redzone = redzoneObject != null && (boolean) jsonObject.get("redzone");
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

        String homeScoreString = homeScore >= 0 ? String.valueOf(homeScore) : "";
        String guestScoreString = guestScore >= 0 ? String.valueOf(guestScore) : "";

        String homeShortString = String.format("%-3s", homeShort);
        if (homeShort.equals(posTeam)) {
            homeShortString = Printer.decorate(homeShortString, Printer.ANSI_WHITE_BOLD, Printer.ANSI_RESET + generateModifiers());
        }

        String guestShortString = String.format("%3s", guestShort);
        if (guestShort.equals(posTeam)) {
            guestShortString = Printer.decorate(guestShortString, Printer.ANSI_WHITE_BOLD, Printer.ANSI_RESET + generateModifiers());
        }

        String stateString = "";
        switch (state) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                stateString = String.format(String.format(" %s&%-2d %s %5s ", Printer.numberAsString(down), togo, Printer.center(yardline, 8), clock));
                break;
            case "Pre":
            case "Pregame":
                stateString = "    NOT YET STARTED    ";
                break;
            case "Halftime":
                stateString = "       HALFTIME        ";
                break;
            case "Suspended":
                stateString = "       SUSPENDED       ";
                break;
            case "Final":
                stateString = "         FINAL         ";
                break;
            case "final overtime":
                stateString = "       FINAL [OT]      ";
                break;
            default:
                stateString = "     UNKNOWN STATE     ";
        }

		if(isRunning()) {
			scoreBox.append("╔═════╤═══════════╤═════╗\n");
			scoreBox.append(String.format("║ %3s │ %s @ %s │ %-3s ║\n",
				guestScoreString, guestShortString, homeShortString, homeScoreString));
			scoreBox.append(String.format("╟─────┴─%s───%s─┴─────╢\n", makeTimeoutsString(homeTimeouts),makeTimeoutsString(guestTimeouts)));
            scoreBox.append(String.format("║%s║\n", stateString));
			scoreBox.append("╚═══════════════════════╝\n");
		} else {
			scoreBox.append("┌─────┬───────────┬─────┐\n");
			scoreBox.append(String.format("│ %3s │ %s @ %s │ %-3s │\n",
				guestScoreString, guestShortString, homeShortString, homeScoreString));
			scoreBox.append("├─────┴───────────┴─────┤\n");
            scoreBox.append(String.format("│%s│\n", stateString));
			scoreBox.append("└───────────────────────┘\n");
		}

		return Printer.decorate(scoreBox.toString(), generateModifiers());
	}

    private String makeTimeoutsString (long timeouts) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            if (i < timeouts) {
                output.append("●");
            } else {
                output.append("○");
            }
        }

        return output.toString();
    }

	private String generateModifiers () {
		StringBuilder modifiers = new StringBuilder();

		if (redzone) {
			modifiers.append(Printer.ANSI_RED_BOLD);
		} else if (isRunning()) {
			modifiers.append(Printer.ANSI_WHITE);
		}

		return modifiers.toString();
	}

}
