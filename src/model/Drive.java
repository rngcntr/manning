package model;

import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;

public class Drive {

	private long id = 0L;
	private String team = "";

	private ArrayList<Play> plays;

	private String result = "";
	private long yards = 0L;
	private long plays = 0L;

	public Drive (long id) {
		this.id = id;
	}

	public static Drive fromJSON (String json) {
		DetailedGame output = null;

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			output = new Game((long) jsonObject.get("eid"));
			output.state = (String) jsonObject.get("q");

			output.homeScore = (long) jsonObject.get("hs");
			output.guestScore = (long) jsonObject.get("vs");

			output.homeShort = (String) jsonObject.get("h");
			output.guestShort = (String) jsonObject.get("v");
			
			output.lastAction = (String) jsonObject.get("ga");
	        //  "gsis": 56933,
	        //  "rz": -1,
		} catch (ParseException pex) {
			System.err.println("Unable to parse Drive from JSON");
			return null;
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse Drive from JSON");
			return null;
		}

		return output; 
	}
}
