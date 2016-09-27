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
				String url = "http://www.nfl.com/liveupdate/scorestrip/ss.json";
				GameList newGameList = GameList.fromJSON(netControl.get(url));
				if (newGameList != null) {
					gameList = newGameList;
					taui.refreshOverview();
				}
			} else {
				String url = "http://www.nfl.com/liveupdate/game-center/" + observedID + "/" + observedID + "_gtd.json";
				DetailedGame newGame = DetailedGame.fromJSON(netControl.get(url), observedID);
				if (newGame != null) {
					detailedGame = newGame;
					taui.refreshSingleView();
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
			gameList = GameList.fromJSON(netControl.get("http://www.nfl.com/liveupdate/scorestrip/ss.json"));
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
