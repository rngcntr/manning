package view;

import model.*;
import jline.console.ConsoleReader;
import java.io.*;

public class Terminal {

	private Manning manning;

	private int lineCount = 0;

	private ConsoleReader console;
	
	private int observedDrive = 0;
	private int observedPlay = 0;

	public Terminal (Manning manning) {
		this.manning = manning;

		try {
			console = new ConsoleReader(System.in, System.out);
		} catch(IOException ioex) {
			System.err.println("Unable to open console streams");
		}

		new Thread (() -> listenForInput()).start();
	}

	synchronized void showLoadingMessage () {
		clearScreen();
		System.out.println(" Loading web resource. Please wait...");
		lineCount = 1;
	}

	synchronized void refreshOverview (GameList newGameList) {
		StringBuilder output = new StringBuilder();
		
		output.append(newGameList.toString());
		String timeStamp = String.format("Last updated: %s", Printer.getCurrentTimeStamp());
		output.append(String.format("\n Press 'q' to exit%34s ", timeStamp));

		clearScreen();
		System.out.println(output.toString());
		lineCount = output.toString().split("\n").length;
	}
	
	synchronized void refreshSingleView (DetailedGame newGame) {
		StringBuilder output = new StringBuilder();
		
		String scoreBox = newGame.getScoreBox();
		String statsBox = newGame.getStatsBox();
		String quarterBox = newGame.getQuarterBox();
		String fieldBox = newGame.getField();
		String driveBox = newGame.getDriveBox(127, observedDrive, observedPlay);

		output.append(Printer.align(Printer.align(scoreBox, 4, statsBox), 4, quarterBox));
		output.append("\n\n");
		output.append(fieldBox);
		output.append("\n\n");
		output.append(driveBox);
		String timeStamp = String.format("Last updated: %s", Printer.getCurrentTimeStamp());
		output.append(String.format("\n\n Press 'q' to exit%108s ", timeStamp));
		
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
			while (true) {
				char input = (char) console.readCharacter(new char[]{'q', 'j', 'k', 'h', 'l'});

				switch (input) {
					case 'q':
						System.exit(0);
						break;
					case 'j':
						observedPlay++;
						manning.refreshSingleView();
						break;
					case 'k':
						observedPlay--;
						manning.refreshSingleView();
						break;
					case 'h':
						observedDrive--;
						manning.refreshSingleView();
						break;
					case 'l':
						observedDrive++;
						manning.refreshSingleView();
						break;
				}
			}
		} catch(IOException ioex) {
			System.exit(0);
		}
	}

}
