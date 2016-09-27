package control;

import model.*;
import view.*;

public class ManningController {

	private NetworkController netControl;

	private TerminalAUI taui;

	private GameList gameList;
	private DetailedGame detailedGame;

	public ManningController () {
		netControl = new NetworkController(this);
	}

	public void run () {
		while (true) {
			//GameList newGameList = GameList.fromJSON(netControl.get("http://www.nfl.com/liveupdate/scorestrip/ss.json"));
			//if (newGameList != null) {
			//	gameList = newGameList;
			//	taui.refreshOverview();
			//}
			DetailedGame newGame = DetailedGame.fromJSON(netControl.get("http://www.nfl.com/liveupdate/game-center/2016020700/2016020700_gtd.json"));
			if (newGame != null) {
				detailedGame = newGame;
				taui.refreshSingleView();
			}

			try {
				Thread.sleep(1000);
			} catch(InterruptedException intex) {
				break;
			}
		}
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
