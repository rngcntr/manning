package view;

import control.*;

public class Manning implements ManningAUI {

	private static final boolean DEBUG = true;

	private ManningController manControl;

	private Terminal terminal;
	private String observedTeam = "";

	public static final int LOADING = 0;
	public static final int OVERVIEW = 1;
	public static final int SINGLE = 2;
	public static final int INPUT = 3;

	private int mode;

	public static void main (String[] args) {
		if (!DEBUG) {
			System.err.close();
		}

		if (args.length > 0) {
			new Manning(args[0].toUpperCase());
		} else {
			new Manning("");
		}
	}

	public Manning (String observedTeam) {
		this.observedTeam = observedTeam;

		terminal = new Terminal(this);
		terminal.showLoadingMessage();

		manControl = new ManningController();
		manControl.setManningAUI(this);
		manControl.observeGame(observedTeam);

		if (manControl.observingGame()) {
			mode = SINGLE;
		} else {
			mode = OVERVIEW;
		}

		manControl.run();
	}

	ManningController getManningController () {
		return manControl;
	}

	void setMode (int mode) {
		if (mode == SINGLE) {
			mode = INPUT;
			String team = terminal.askForTeam();
			mode = LOADING;
			terminal.showLoadingMessage();
			manControl.observeGame(team);

			if (manControl.observingGame()) {
				mode = SINGLE;
			} else {
				mode = OVERVIEW;
			}
		}

		this.mode = mode;
	}

	int getMode () {
		return mode;
	}

	public void update () {
		switch (mode) {
			case OVERVIEW:
				terminal.refreshOverview(manControl.getGameList());
				break;
			case SINGLE:
				terminal.refreshSingleView(manControl.getDetailedGame());
				break;
			case INPUT:
				break;
			case LOADING:
				break;
			default:
				break;
		}
	}

	public void quit () {
		System.exit(0);
	}
}
