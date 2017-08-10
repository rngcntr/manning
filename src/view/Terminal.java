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

	synchronized String askForTeam () {
		manning.setMode(Manning.INPUT);
		clearScreen();
		try {
			String team = console.readLine("Select a team > ");
			lineCount = 1;
			return team.toUpperCase();
		} catch (IOException ioex) {
			return "";
		}
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
		if (driveCount > 0) {
			observedDrive %= driveCount;
			observedDrive += driveCount;
			observedDrive %= driveCount;
		} else {
			observedDrive = 0;
		}

		Drive observedD = game.getDrive(observedDrive);
		int playCount = observedD.getPlayCount();
		if (playCount > 0) {
			observedPlay %= playCount;
			observedPlay += playCount;
			observedPlay %= playCount;
		} else {
			observedPlay = 0;
		}
	}

	private void clearScreen () {
		while (lineCount > 0) {
			System.out.printf("\033[A");
			System.out.printf("\033[2K");
			lineCount--;
		}
	}

	private void listenForInput () {
		try {
			while (true) {
				char input = (char) console.readCharacter(new char[]{'q', 'w', 'a', 's', 'd', '0', 'c'});

				switch (input) {
					case 'q':
						manning.quit();
						break;
					case 's':
						if (manning.getMode() == Manning.SINGLE) {
							observedPlay++;
							manning.update();
						}
						break;
					case 'w':
						if (manning.getMode() == Manning.SINGLE) {
							observedPlay--;
							manning.update();
						}
						break;
					case 'a':
						if (manning.getMode() == Manning.SINGLE) {
							observedDrive++;
							observedPlay = 0;
							manning.update();
						}
						break;
					case 'd':
						if (manning.getMode() == Manning.SINGLE) {
							observedDrive--;
							observedPlay = 0;
							manning.update();
						}
						break;
					case '0':
						if (manning.getMode() == Manning.SINGLE) {
							observedDrive = 0;
							observedPlay = 0;
							manning.update();
						}
						break;
					case 'c':
						if (manning.getMode() == Manning.SINGLE) {
							manning.setMode(Manning.OVERVIEW);
							manning.update();
						} else if (manning.getMode() == Manning.OVERVIEW) {
							manning.setMode(Manning.SINGLE);
							// Do not update, wait for resources to load.
						}
						break;
				}
			}
		} catch(IOException ioex) {
			System.exit(0);
		}
	}
}
