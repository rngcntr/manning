package view;

import control.*;

public class Manning implements TerminalAUI {

	private ManningController manControl;

	private Terminal terminal;

	public static void main (String[] args) {
		new Manning();
	}

	public Manning () {
		terminal = new Terminal(this);

		manControl = new ManningController();
		manControl.setTerminalAUI(this);
		manControl.run();
	}

	public void refreshOverview () {
		terminal.refreshOverview (manControl.getGameList());
	}

	public void refreshSingleView () {
		terminal.refreshSingleView (manControl.getDetailedGame());
	}
}
