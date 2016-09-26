package control;

import model.*;
import view.*;

public class ManningController {

	private NetworkController netControl;

	private TerminalAUI taui;

	private GameList gameList;

	public ManningController () {
		netControl = new NetworkController(this);
	}

	public void run () {
		while (true) {
			gameList = GameList.fromJSON(netControl.get("http://www.nfl.com/liveupdate/scorestrip/ss.json"));
			taui.refreshOverview();

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

}
