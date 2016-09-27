package model;

import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;
import java.lang.Math;

public class DetailedGame {

	private Team home = null;
	private Team guest = null;

	private String state = "";

	//private ArrayList<Drive> drives;

	private String yardLine = "";
	private long down = 0;
	private long toGo = 0;
	private String clock = "";
	private String posTeam = "";

	public static DetailedGame fromJSON (String json, long id) {
		DetailedGame output = new DetailedGame();

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			JSONObject game = (JSONObject) jsonObject.get(String.valueOf(id));

			JSONObject homeTeam = (JSONObject) game.get("home");
			output.home = Team.fromJSON(homeTeam.toJSONString());

			JSONObject guestTeam = (JSONObject) game.get("away");
			output.guest = Team.fromJSON(guestTeam.toJSONString());

			output.state = (String) game.get("qtr");
			output.yardLine = (String) game.get("yl");
			output.down = (long) game.get("down");
			output.toGo = (long) game.get("togo");
			output.clock = (String) game.get("clock");
			output.posTeam = (String) game.get("posteam");
		} catch (ParseException pex) {
			System.err.println("Unable to parse DetailedGame from JSON");
			return null;
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse DetailedGame from JSON");
			return null;
		}

		return output; 
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

	public String getScoreBox () {
		StringBuilder scoreBox = new StringBuilder();

		scoreBox.append(" ╔═══════╤═══════════╤═══════╗ \n");
		scoreBox.append(String.format(" ║  %3s %s│   %5s   │%s %-3s  ║ \n",
					guest.getName(), posTeam.equals(guest.getName()) ? "◀" : " ",
					clock, posTeam.equals(home.getName()) ? "▶" : " ", home.getName()));
		scoreBox.append(String.format(" ║  %3s  │    %3s    │  %-3s  ║ \n",
					guest.getScore(), Printer.numberAsString(state), home.getScore()));
		scoreBox.append(" ╟───────┴───────────┴───────╢ \n");
		if (state.equals("Final")) {
			scoreBox.append(String.format(" ║%s    Final    %s║ \n",
						guest.getTimeoutsAsString(true), home.getTimeoutsAsString(false)));
		} else {
			scoreBox.append(String.format(" ║%s  %3s & %2d   %s║ \n",
						guest.getTimeoutsAsString(true), Printer.numberAsString(down),
						toGo, home.getTimeoutsAsString(false)));
		}
		scoreBox.append(" ╚═══════════════════════════╝ \n");

		return Printer.decorate(scoreBox.toString(), generateDefaultModifiers());
	}

	public String getStatsBox () {
		StringBuilder statsBox = new StringBuilder();

		long maximum = Math.max(guest.getYards(), home.getYards());

		String guestYardsBar = Printer.generateBar(Math.round((double) guest.getYards() / maximum * 15.0),
				15, Printer.ANSI_BACK_WHITE, generateDefaultModifiers(), false);
		String guestPassBar = Printer.generateBar(Math.round((double) guest.getPassYards() / maximum * 15.0),
				15, Printer.ANSI_BACK_WHITE, generateDefaultModifiers(), false);
		String guestRushBar = Printer.generateBar(Math.round((double) guest.getRushYards() / maximum * 15.0),
				15, Printer.ANSI_BACK_WHITE, generateDefaultModifiers(), false);

		String homeYardsBar = Printer.generateBar(Math.round((double) home.getYards() / maximum * 15.0),
				15, Printer.ANSI_BACK_WHITE, generateDefaultModifiers(), true);
		String homePassBar = Printer.generateBar(Math.round((double) home.getPassYards() / maximum * 15.0),
				15, Printer.ANSI_BACK_WHITE, generateDefaultModifiers(), true);
		String homeRushBar = Printer.generateBar(Math.round((double) home.getRushYards() / maximum * 15.0),
				15, Printer.ANSI_BACK_WHITE, generateDefaultModifiers(), true);

		statsBox.append(" ╔═════════════════════╤═════════╤═════════════════════╗ \n");
		statsBox.append(String.format(" ║                 %-3s │  YARDS  │ %3s                 ║ \n",
					guest.getName(), home.getName()));
		statsBox.append(String.format(" ║ %s %3d │  total  │ %-3d %s ║ \n",
					guestYardsBar, guest.getYards(), home.getYards(), homeYardsBar));
		statsBox.append(String.format(" ║ %s %3d │ passing │ %-3d %s ║ \n",
					guestPassBar, guest.getPassYards(), home.getPassYards(), homePassBar));
		statsBox.append(String.format(" ║ %s %3d │ rushing │ %-3d %s ║ \n",
					guestRushBar, guest.getRushYards(), home.getRushYards(), homeRushBar));
		statsBox.append(" ╚═════════════════════╧═════════╧═════════════════════╝ ");

		return Printer.decorate(statsBox.toString(), generateDefaultModifiers());
	}

	public String getField () {
		String[] field = new String[] {
			"                                                                                                                               \n",
			"   ╔═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╗   \n",
			"   ║         │        1│0       2│0       3│0       4│0       5│0       4│0       3│0       2│0       1│0        │         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			String.format("   ║   %3s   │         │         │         │         │         │         │         │         │         │         │   %-3s   ║   \n",
					guest.getName(), home.getName()),
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │        1│0       2│0       3│0       4│0       5│0       4│0       3│0       2│0       1│0        │         ║   \n",
			"   ╚═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╝   \n",
			"                                                                                                                               "};

		StringBuilder output = new StringBuilder();

		for (String line : field) {
			output.append(line);
		}

		return Printer.decorate(output.toString(), generateFieldModifiers());
	}

	private String generateDefaultModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_BACK_BLACK);
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}

	private String generateFieldModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_BACK_GREEN);
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}
}
