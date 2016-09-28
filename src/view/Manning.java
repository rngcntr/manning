package view;

import control.*;

public class Manning implements TerminalAUI {

	private static final boolean DEBUG = true;

	private ManningController manControl;

	private Terminal terminal;
	private String observedTeam = "";

	public static void main (String[] args) {
		if (!DEBUG) {
			System.err.close();
		}

		if (args.length > 0) {
			new Manning(args[0]);
		} else {
			new Manning("");
		}
	}

	public Manning (String observedTeam) {
		this.observedTeam = observedTeam;

		terminal = new Terminal(this);

		manControl = new ManningController();
		manControl.setTerminalAUI(this);
		manControl.observeGame(observedTeam);
		manControl.run();
	}

	public void showLoadingMessage () {
		terminal.showLoadingMessage();
	}

	public void refreshOverview () {
		terminal.refreshOverview(manControl.getGameList());
	}

	public void refreshSingleView () {
		terminal.refreshSingleView(manControl.getDetailedGame());
	}
}
