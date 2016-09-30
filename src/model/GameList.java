package model;

import java.util.*;
import org.json.simple.*;

public class GameList {

	private long week = 0;
	private ArrayList<Game> games;

	public GameList () {
		this.games = new ArrayList<Game>();
	}

	public static GameList fromJSON (JSONObject jsonObject) {
		GameList output = new GameList();

		try {
			long week = (long) jsonObject.get("w");
			output.week = week;

			JSONArray games = (JSONArray) jsonObject.get("gms");
			Iterator iterator = games.iterator();

			while (iterator.hasNext()) {
				JSONObject nextGameJSON = (JSONObject) iterator.next();
				Game nextGame = Game.fromJSON(nextGameJSON);
				output.games.add(nextGame);
			}
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse GameList from JSON");
			return null;
		}

		return output; 
	}

	public long getGame (String team) {
		for (Game game : games) {
			if (team.equals(game.getHome()) || team.equals(game.getGuest())) {
				return game.getID();
			}
		}

		return -1L;
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

		return output.toString();
	}
}
