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
		synchronizeObservers(newGame);
		String fieldBox = newGame.getField(observedDrive, observedPlay);
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

	private void synchronizeObservers (DetailedGame game) {
		int driveCount = game.getDriveCount();
		observedDrive %= driveCount;
		observedDrive += driveCount;
		observedDrive %= driveCount;

		Drive observedD = game.getDrive(observedDrive);
		int playCount = observedD.getPlayCount();
		observedPlay %= playCount;
		observedPlay += playCount;
		observedPlay %= playCount;
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
				char input = (char) console.readCharacter(new char[]{'q', 'w', 'a', 's', 'd', '0'});

				switch (input) {
					case 'q':
						System.exit(0);
						break;
					case 's':
						observedPlay++;
						manning.refreshSingleView();
						break;
					case 'w':
						observedPlay--;
						manning.refreshSingleView();
						break;
					case 'a':
						observedDrive++;
						observedPlay = 0;
						manning.refreshSingleView();
						break;
					case 'd':
						observedDrive--;
						observedPlay = 0;
						manning.refreshSingleView();
						break;
					case '0':
						observedDrive = 0;
						observedPlay = 0;
						manning.refreshSingleView();
						break;
				}
			}
		} catch(IOException ioex) {
			System.exit(0);
		}
	}

}
