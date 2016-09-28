package model;

import org.json.simple.*;
import java.util.*;

public class Drive {

	private String team = "";

	private ArrayList<Play> plays;

	private String result = "";
	private long yards = 0L;
	private long numPlays = 0L;

	public static Drive fromJSON (JSONObject jsonObject) {
		Drive output = new Drive();

		try {
			output.team = (String) jsonObject.get("posteam");
			output.result = (String) jsonObject.get("result");
			output.yards = (long) jsonObject.get("ydsgained");
			output.numPlays = (long) jsonObject.get("numplays");

			JSONObject jsonPlays = (JSONObject) jsonObject.get("plays");
			output.plays = new ArrayList<Play>();
			for (Object key : jsonPlays.keySet()) {
				JSONObject jsonPlay = (JSONObject) jsonPlays.get((String) key);
				Play newPlay = Play.fromJSON(jsonPlay);
				output.plays.add(newPlay);
			}
		} catch (NullPointerException npex) {
			npex.printStackTrace();
			System.err.println("Unable to parse Drive from JSON");
			return null;
		}

		return output; 
	}

	public String toString (int width) {
		String left = String.format("%3s: %2d plays, %3d yards ", team, numPlays, yards);
		
		int space = width - left.length() - result.length();
		if (space < 0) {
			return "";
		}

		String output = String.format("%s%s%s", left, Printer.generateSpace(space), result);
		return Printer.decorate(output, generateDefaultModifiers());
	}

	private String generateDefaultModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}
}
