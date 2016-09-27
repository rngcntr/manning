package model;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Team {

	private long q1Score;
	private long q2Score;
	private long q3Score;
	private long q4Score;
	private long otScore;
	private long totalScore;

	private String name = "";
	private long timeouts;
	private long firstDowns;
	private long yards;
	private long passYards;
	private long rushYards;
	private long penalties;
	private long penaltyYards;
	private long turnovers;

	public static Team fromJSON (String json) {
		Team output = new Team();

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			output.name = (String) jsonObject.get("abbr");
			output.timeouts = (long) jsonObject.get("to");
			
			JSONObject score = (JSONObject) jsonObject.get("score");
			output.q1Score = (long) score.get("1");
			output.q2Score = (long) score.get("2");
			output.q3Score = (long) score.get("3");
			output.q4Score = (long) score.get("4");
			output.otScore = (long) score.get("5");
			output.totalScore = (long) score.get("T");

			JSONObject stats = (JSONObject) jsonObject.get("stats");
			JSONObject teamStats = (JSONObject) stats.get("team");
			output.firstDowns = (long) teamStats.get("totfd");
			output.yards = (long) teamStats.get("totyds");
			output.passYards = (long) teamStats.get("pyds");
			output.rushYards = (long) teamStats.get("ryds");
			output.penalties = (long) teamStats.get("pen");
			output.penaltyYards = (long) teamStats.get("penyds");
			output.turnovers = (long) teamStats.get("trnovr");
		} catch (ParseException pex) {
			System.err.println("Unable to parse Team from JSON");
			return null;
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse Team from JSON");
			return null;
		}

		return output; 
	}

	public String getName () {
		return name;
	}

	public long getTimeOuts () {
		return timeouts;
	}

	public String getTimeoutsAsString (boolean orientation) {
		// true for left team, false for right team
		StringBuilder output = new StringBuilder();
		output.append(" ");

		StringBuilder used = new StringBuilder();
		for (int count = 1; count <= (3 - timeouts); count++) {
			used.append("○ ");
		}

		StringBuilder remaining = new StringBuilder();
		for (int count = 1; count <= timeouts; count++) {
			remaining.append("● ");
		}

		if (orientation) {
			output.append(used.toString());
			output.append(remaining.toString());
		} else {
			output.append(remaining.toString());
			output.append(used.toString());
		}

		return output.toString();
	}

	public long getScore () {
		return totalScore;
	}

	public long getYards () {
		return yards;
	}

	public long getPassYards () {
		return passYards;
	}

	public long getRushYards () {
		return rushYards;
	}
}
