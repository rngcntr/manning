package view;

import model.*;
import jline.console.ConsoleReader;
import java.io.*;

public class Terminal {

	private Manning manning;

	private GameList currentGameList = null;
	private int lineCount = 0;

	private ConsoleReader console;

	public Terminal (Manning manning) {
		this.manning = manning;

		try {
			console = new ConsoleReader(System.in, System.out);
		} catch(IOException ioex) {
			System.err.println("Unable to open console streams");
		}

		new Thread (() -> listenForInput()).start();
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

	private void listenForInput () {
		try {
			char input = (char) console.readCharacter(new char[]{'q'});
			if (input == 'q') {
				System.exit(0);
			}
		} catch(IOException ioex) {
			System.exit(0);
		}
	}

}
