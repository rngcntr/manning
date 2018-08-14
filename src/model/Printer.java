package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Math;

public class Printer {

	public static final String ANSI_RESET = "\u001B[0m";

	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BOLD = "\u001B[30;1m";
	public static final String ANSI_RED_BOLD = "\u001B[31;1m";
	public static final String ANSI_GREEN_BOLD = "\u001B[32;1m";
	public static final String ANSI_YELLOW_BOLD = "\u001B[33;1m";
	public static final String ANSI_BLUE_BOLD = "\u001B[34;1m";
	public static final String ANSI_PURPLE_BOLD = "\u001B[35;1m";
	public static final String ANSI_CYAN_BOLD = "\u001B[36;1m";
	public static final String ANSI_WHITE_BOLD = "\u001B[37;1m";

	public static final String ANSI_BACK_BLACK = "\u001B[40m";
	public static final String ANSI_BACK_RED = "\u001B[41m";
	public static final String ANSI_BACK_GREEN = "\u001B[42m";
	public static final String ANSI_BACK_YELLOW = "\u001B[43m";
	public static final String ANSI_BACK_BLUE = "\u001B[44m";
	public static final String ANSI_BACK_PURPLE = "\u001B[45m";
	public static final String ANSI_BACK_CYAN = "\u001B[46m";
	public static final String ANSI_BACK_WHITE = "\u001B[47m";

	public static final String ANSI_SAVE = "\u001B[?47h";
	public static final String ANSI_RESTORE = "\u001B[?47l";
	public static final String ANSI_CLEAR = "\u001B[2J";
	public static final String ANSI_HOME = "\u001B[H";

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static String generateBar (long length, long totalLength, String option, String closeOption, boolean startsLeft) {
		
		StringBuilder output = new StringBuilder();

		if (startsLeft) {
			output.append(option);
			output.append(generateSpace(length));
			output.append(closeOption);
			output.append(generateSpace(totalLength - length));
		} else {
			output.append(generateSpace(totalLength - length));
			output.append(option);
			output.append(generateSpace(length));
			output.append(closeOption);
		}

		return output.toString();
	}

	public static String generateSpace (long length) {
		StringBuilder output = new StringBuilder();

		if (length > 0) {
			for (long count = 0; count < length; count++) {
				output.append(" ");
			}
		}

		return output.toString();
	}

	public static String decorate (String input, String option) {
		StringBuilder output = new StringBuilder();
		String[] splitted = input.split("\n");

		for (int count = 0; count < splitted.length - 1; count++) {
			output.append(String.format("%s%s%s\n", option, splitted[count], ANSI_RESET));
		}

		output.append(String.format("%s%s%s", option, splitted[splitted.length-1], ANSI_RESET));

		return output.toString();
	}

	public static String fit (String input, int width, int height) {
		StringBuilder output = new StringBuilder();
		String[] splittedIn = input.split("\n");
		String[] splittedOut = new String[height];

		for (int line = 0; line < height; line++) {
			if (line < splittedIn.length) {
				int length = splittedIn[line].length();
				splittedOut[line] = String.format("%s%s", splittedIn[line], generateSpace(width - length));
				splittedOut[line] = splittedOut[line].substring(0, width - 1);
			} else {
				splittedOut[line] = generateSpace(width);
			}
		}

		for (int line = 0; line < splittedOut.length - 1; line++) {
			output.append(String.format("%s\n", splittedOut[line]));
		}

		output.append(splittedOut[splittedOut.length - 1]);

		return output.toString();
	}

	public static String decorate (String input, String option, String closeOption) {
		StringBuilder output = new StringBuilder();
		String[] splitted = input.split("\n");

		for (int count = 0; count < splitted.length - 1; count++) {
			output.append(String.format("%s%s%s\n", option, splitted[count], closeOption));
		}

		output.append(String.format("%s%s%s", option, splitted[splitted.length-1], closeOption));

		return output.toString();
	}

	public static String align (String left, int space, String right) {
		String[] leftSplitted = left.split("\n");
		String[] rightSplitted = right.split("\n");
		StringBuilder output = new StringBuilder();

		for (int line = 0; line < leftSplitted.length && line < rightSplitted.length; line++) {
			output.append(leftSplitted[line]);
			output.append(generateSpace(space));
			output.append(rightSplitted[line]);

			if (line < leftSplitted.length - 1 && line < rightSplitted.length - 1) {
				output.append("\n");
			}
		}

		return output.toString();
	}

	public static String numberAsString (long number) {
		int intNum = Math.toIntExact(number);
		switch (intNum) {
			case 1:
				return "1st";
			case 2:
				return "2nd";
			case 3:
				return "3rd";
			case 4:
				return "4th";
			case 5:
				return " OT";
			default:
				return "   ";
		}
	}

	public static String numberAsString (String number) {
		try {
			int intNum = Integer.valueOf(number);
			return numberAsString(intNum);
		} catch (NumberFormatException nfex) {
			return "   ";
		}
	}
	
	public static String breakLines (String input, int width) {
		input += "\n"; // Needed to handle last line correctly
		input = input.replaceAll(String.format("(.{1,%d})\\s+", width), "$1\n");
		return input;
	}
}
