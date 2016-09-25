package view;

import model.*;

public class Terminal {

	private Manning manning;

	private GameList currentGameList = null;
	private int lineCount = 0;

	public Terminal (Manning manning) {
		this.manning = manning;
	}

	void refreshOverview (GameList newGameList) {
		String output = newGameList.toString();

		if(lineCount > 0) {
			System.out.printf("\033[%dA", lineCount);
			System.out.printf("\033[2K");
		}

		System.out.print(output);
		currentGameList = newGameList;
		lineCount = output.split("\n").length;
	}

}
