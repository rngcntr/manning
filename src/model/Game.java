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

	        //  "gsis": 56933,
	        //  "ga": "",
	        //  "rz": -1,

			return output;
		} catch (ParseException e) {
			e.printStackTrace();
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
		// TODO: Change to check status
		return time.equals("1:00");
	}

	public String toString () {
		StringBuilder scoreBox = new StringBuilder();
		if(isRunning()) {
			scoreBox.append("╔═════╤═══════════╤═════╗\n");
			scoreBox.append(String.format("║ %3d │ %3s @ %-3s │ %-3d ║\n", guestScore, guestShort, homeShort, homeScore));
			scoreBox.append("╚═════╧═══════════╧═════╝\n");
		} else {
			scoreBox.append("┌─────┬───────────┬─────┐\n");
			scoreBox.append(String.format("│ %3d │ %3s @ %-3s │ %-3d │\n", guestScore, guestShort, homeShort, homeScore));
			scoreBox.append("└─────┴───────────┴─────┘\n");
		}

		return scoreBox.toString();
	}

}
