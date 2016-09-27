package view;

import model.*;
import jline.console.ConsoleReader;
import java.io.*;

public class Terminal {

	private Manning manning;

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
		StringBuilder output = new StringBuilder();
		
		output.append(newGameList.toString());
		String timeStamp = String.format("Last updated: %s", Printer.getCurrentTimeStamp());
		output.append(String.format("\n Press 'q' to exit%34s ", timeStamp));

		clearScreen();
		System.out.print(output.toString());
		lineCount = output.toString().split("\n").length;
	}
	
	void refreshSingleView (DetailedGame newGame) {
		StringBuilder output = new StringBuilder();
		
		String scoreBox = newGame.getScoreBox();
		String statsBox = newGame.getStatsBox();
		String fieldBox = newGame.getField();

		output.append(Printer.align(scoreBox, 4, statsBox));
		output.append("\n\n");
		output.append(fieldBox);
		String timeStamp = String.format("Last updated: %s", Printer.getCurrentTimeStamp());
		output.append(String.format("\n Press 'q' to exit%34s ", timeStamp));
		
		clearScreen();
		System.out.println(output.toString());
		lineCount = output.toString().split("\n").length;
	}

	private void clearScreen () {
		if(lineCount > 0) {
			System.out.printf("\033[%dA", lineCount);
			System.out.printf("\033[2K");
		}
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
