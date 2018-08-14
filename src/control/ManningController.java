package control;

import model.*;
import view.*;

public class ManningController {

	private NetworkController netControl;

	private TerminalAUI taui;

	private GameList gameList;
	private DetailedGame detailedGame;
	private long observedID = -1L;

	public ManningController () {
		netControl = new NetworkController(this);
	}

	public void run () {
		while (true) {
			if (observedID == -1L) {
				String url = "http://www.nfl.com/liveupdate/scores/scores.json";
				String json = netControl.get(url);
				GameList newGameList = GameList.fromJSON(netControl.parse(json));
				if (newGameList != null) {
					gameList = newGameList;
					taui.refreshOverview();
				}
			} else {
				String url = "http://www.nfl.com/liveupdate/game-center/" + observedID + "/" + observedID + "_gtd.json";
				String json = netControl.get(url);
				if (json == null) {
					observedID = -1L;
					continue;
				}
				org.json.simple.JSONObject jsonObject = netControl.parse(json);
				DetailedGame newGame = DetailedGame.fromJSON(jsonObject, observedID);
				if (newGame != null) {
					detailedGame = newGame;
					taui.refreshSingleView();
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
		taui.showLoadingMessage();

		while (gameList == null) {
			String json = netControl.get("http://www.nfl.com/liveupdate/scores/scores.json");
			gameList = GameList.fromJSON(netControl.parse(json));
		}
		
		observedID = gameList.getGame(team);
	}

	public void setTerminalAUI (TerminalAUI taui) {
		this.taui = taui;
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
