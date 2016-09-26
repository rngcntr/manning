package model;

import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class GameList {

	private long week = 0;
	private ArrayList<Game> games;

	public GameList () {
		this.games = new ArrayList<Game>();
	}

	public static GameList fromJSON (String json) {
		GameList output = new GameList();

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			long week = (long) jsonObject.get("w");
			output.week = week;

			JSONArray games = (JSONArray) jsonObject.get("gms");
			Iterator iterator = games.iterator();

			while (iterator.hasNext()) {
				JSONObject nextGameJSON = (JSONObject) iterator.next();
				Game nextGame = Game.fromJSON(nextGameJSON.toJSONString());
				if(nextGame == null) throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
				output.games.add(nextGame);
			}
		} catch (ParseException e) {
			System.err.println("Unable to parse GameList");
			return null;
		}

		return output; 
	}

	public String toString() {
		StringBuilder output = new StringBuilder();

		output.append("╔═══════════════════════════════════════════════════╗\n");
		output.append(String.format("║                    NFL Week %2d                    ║\n", week));
		output.append("╚═══════════════════════════════════════════════════╝\n");

		Iterator<Game> iterator = games.iterator();
		while(iterator.hasNext()) {
			Game nextGame = iterator.next();

			if(iterator.hasNext()) {
				Game alignedGame = iterator.next();
				String[] firstSplittedGame = nextGame.toString().split("\n");
				String[] secondSplittedGame = alignedGame.toString().split("\n");

				for(int i = 0; i < firstSplittedGame.length; i++) {
					output.append(firstSplittedGame[i]);
					output.append("   ");
					output.append(secondSplittedGame[i]);
					output.append("\n");
				}
			} else {
				output.append(nextGame);
			}
		}

		String timeStamp = String.format("Last updated: %s", Printer.getCurrentTimeStamp());
		output.append(String.format("\nPress 'q' to exit%36s\n", timeStamp));

		return output.toString();
	}
}
