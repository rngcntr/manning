package model;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Game {

	private long homeScore = 0;
	private long guestScore = 0;
	
	private String homeShort = "";
	private String guestShort = "";

	private String homeLong = "";
	private String guestLong = "";
	
	private String day = "";
	private String time = "";

	private long id = 0L;
	private String state = "";

	private String lastAction = "";

	public Game (long id) {
		this.id = id;
	}

	public static Game fromJSON (String json) {
		Game output = null;

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			output = new Game((long) jsonObject.get("eid"));
			output.state = (String) jsonObject.get("q");

			output.homeScore = (long) jsonObject.get("hs");
			output.guestScore = (long) jsonObject.get("vs");

			output.homeShort = (String) jsonObject.get("h");
			output.guestShort = (String) jsonObject.get("v");
			
			output.homeLong = (String) jsonObject.get("hnn");
			output.guestLong = (String) jsonObject.get("vnn");

			output.day = (String) jsonObject.get("d");
			output.time = (String) jsonObject.get("t");

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

	public void setHomeScore (long homeScore) {
		this.homeScore = homeScore;
	}

	public void setGuestScore (long guestScore) {
		this.guestScore = guestScore;
	}

	public void setHomeShort (String homeShort) {
		this.homeShort = homeShort;
	}

	public void setGuestShort (String guestShort) {
		this.guestShort = guestShort;
	}

	public void setHomeLong (String homeLong) {
		this.homeLong = homeLong;
	}

	public void setGuestLong (String guestLong) {
		this.guestLong = guestLong;
	}

	public void setDay (String day) {
		this.day = day;
	}

	public void setTime (String time) {
		this.time = time;
	}

	public void setState (String state) {
		this.state = state;
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
			scoreBox.append(String.format("%s╔═════╤═══════════╤═════╗%s\n", generateModifiers(), Printer.ANSI_RESET));
			scoreBox.append(String.format("%s║ %3d │ %3s @ %-3s │ %-3d ║%s\n",
				generateModifiers(), guestScore, guestShort, homeShort, homeScore, Printer.ANSI_RESET));
			scoreBox.append(String.format("%s╚═════╧═══════════╧═════╝%s\n", generateModifiers(), Printer.ANSI_RESET));
		} else {
			scoreBox.append(String.format("%s┌─────┬───────────┬─────┐%s\n", generateModifiers(), Printer.ANSI_RESET));
			scoreBox.append(String.format("%s│ %3d │ %3s @ %-3s │ %-3d │%s\n",
				generateModifiers(), guestScore, guestShort, homeShort, homeScore, Printer.ANSI_RESET));
			scoreBox.append(String.format("%s└─────┴───────────┴─────┘%s\n", generateModifiers(), Printer.ANSI_RESET));
		}

		return scoreBox.toString();
	}

	private String generateModifiers () {
		StringBuilder modifiers = new StringBuilder();

		if (!lastAction.equals("")) {
			modifiers.append(Printer.ANSI_GREEN_BOLD);
		} else if (isRunning()) {
			modifiers.append(Printer.ANSI_WHITE_BOLD);
		}

		return modifiers.toString();
	}

}
