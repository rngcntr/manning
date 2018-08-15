package control;

import model.*;
import view.*;

public class ManningController {

	private NetworkController netControl;

	private ManningAUI maui;

	private GameList gameList;
	private DetailedGame detailedGame;
	private long observedID = -1L;

	public ManningController () {
		netControl = new NetworkController(this);
	}

	public void run () {
		while (true) {
			long currentID = observedID;
			if (observedID == -1L) {
				String url = "http://www.nfl.com/liveupdate/scores/scores.json";
				String json = netControl.get(url);
				GameList newGameList = GameList.fromJSON(netControl.parse(json));
				if (newGameList != null) {
					gameList = newGameList;
					maui.update();
				}
			} else {
                // remove this v
                currentID = 2018081152L;
				String url = "http://www.nfl.com/liveupdate/game-center/" + currentID + "/" + currentID + "_gtd.json";
				String json = netControl.get(url);
				if (json == null) {
					observedID = -1L;
					continue;
				}
				org.json.simple.JSONObject jsonObject = netControl.parse(json);
				DetailedGame newGame = DetailedGame.fromJSON(jsonObject, currentID);
				if (newGame != null) {
					detailedGame = newGame;
					maui.update();
				} else {
					observedID = -1L;
					continue;
				}
			}

			try {
				Thread.sleep(1000);
			} catch(InterruptedException intex) {
				break;
			}
		}
	}

	public void observeGame (String team) {
		while (gameList == null) {
			String json = netControl.get("http://www.nfl.com/liveupdate/scores/scores.json");
			gameList = GameList.fromJSON(netControl.parse(json));
		}
		
		observedID = gameList.getGame(team);
	}

	public boolean observingGame () {
		return observedID != -1L;
	}

	public void setManningAUI (ManningAUI maui) {
		this.maui = maui;
	}

	public NetworkController getNetworkController () {
		return netControl;
	}

	public GameList getGameList () {
		return gameList;
	}
	
	public DetailedGame getDetailedGame () {
		return detailedGame;
	}
}
