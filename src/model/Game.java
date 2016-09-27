package model;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Game {

	private long homeScore = 0;
	private long guestScore = 0;
	
	private String homeShort = "";
	private String guestShort = "";

	private long id = 0L;
	private String state = "";

	private String lastAction = "";

	public static Game fromJSON (String json) {
		Game output = new Game();

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			output.id = (long) jsonObject.get("eid");
			output.state = (String) jsonObject.get("q");

			output.homeScore = (long) jsonObject.get("hs");
			output.guestScore = (long) jsonObject.get("vs");

			output.homeShort = (String) jsonObject.get("h");
			output.guestShort = (String) jsonObject.get("v");
			
			output.lastAction = (String) jsonObject.get("ga");
	        //  "gsis": 56933,
	        //  "rz": -1,
		} catch (ParseException pex) {
			System.err.println("Unable to parse Game from JSON");
			return null;
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

		if (!lastAction.equals("") && !lastAction.equals("INT")) {
			modifiers.append(Printer.ANSI_GREEN_BOLD);
		} else if (isRunning()) {
			modifiers.append(Printer.ANSI_WHITE_BOLD);
		}

		return modifiers.toString();
	}

}
