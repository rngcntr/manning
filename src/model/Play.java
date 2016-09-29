package model;

import org.json.simple.*;

public class Play {

	private long quarter = 0L;
	private long down = 0L;
	private String time = "";
	private String yardLine = "";
	private long toGo = 0L;
	private String driveStart = "";
	private long yards = 0L;
	private String team = "";

	private String desc = "";
	private String note = "";

	public static Play fromJSON (JSONObject jsonObject) {
		Play output = new Play();

		try {
			output.quarter = (long) jsonObject.get("qtr");
			output.down = (long) jsonObject.get("down");
			output.time = (String) jsonObject.get("time");
			output.yardLine = (String) jsonObject.get("yrdln");
			output.toGo = (long) jsonObject.get("ydstogo");
			output.yards = (long) jsonObject.get("ydsnet");
			output.team = (String) jsonObject.get("posteam");
			
			output.desc = (String) jsonObject.get("desc");
			output.note = (String) jsonObject.get("note");
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse Play from JSON");
			return null;
		}

		return output; 
	}

	public void setStart (String startLine) {
		this.driveStart = startLine;
	}

	public String toString (int width, boolean current) {
		String right = desc.replaceAll("\\([0-9]*:[0-9]*\\) ", "");
		right = String.format("%s", right);
		right = Printer.breakLines(right, width - 12);
		right = Printer.fit(right, width - 12, 3);

		String left;
		if (down != 0) {
			left = String.format("[%5s]\n%s & %d\n%s", time, Printer.numberAsString(down), toGo, yardLine);
		} else {
			left = String.format("[%5s]", time);
		}

		left = Printer.fit(left, 11, 3);
		String output = Printer.align(left, 1, right);
		return Printer.decorate(output, generateModifiers(current));
	}

	public String getField (String home, String guest) {
		StringBuilder upperSideline = new StringBuilder();
		StringBuilder lowerSideline = new StringBuilder();
		if (team.equals(guest)) {
			upperSideline.append(Printer.generateSpace(13 + yardLineToInt(driveStart, home, guest) + yards));
			lowerSideline.append(Printer.generateSpace(13 + yardLineToInt(driveStart, home, guest) + yards));
		} else {
			upperSideline.append(Printer.generateSpace(13 + yardLineToInt(driveStart, home, guest) - yards));
			lowerSideline.append(Printer.generateSpace(13 + yardLineToInt(driveStart, home, guest) - yards));
		}

		upperSideline.append("▼");
		lowerSideline.append("▲");

		String[] field = new String[] {
			"   ╔═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╤═════════╗   \n",
			"   ║         │        1│0       2│0       3│0       4│0       5│0       4│0       3│0       2│0       1│0        │         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			String.format("   ║   %3s   │         │         │         │         │         │         │         │         │         │         │   %-3s   ║   \n",
					guest, home),
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│|||||||||│         ║   \n",
			"   ║         │         │         │         │         │         │         │         │         │         │         │         ║   \n",
			"   ║         │        1│0       2│0       3│0       4│0       5│0       4│0       3│0       2│0       1│0        │         ║   \n",
			"   ╚═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╝   \n"};

		StringBuilder output = new StringBuilder();

		output.append(String.format("%-127s\n", upperSideline.toString()));
		for (String line : field) {
			String leftPart;
			String middlePart;
			String rightPart;
			int scrimmage = 13 + yardLineToInt(yardLine, home, guest);
			int firstDown = 13 + yardLineToInt(yardLine, home, guest);
			if (team.equals(guest)) {
				if (down != 0) {
					firstDown += toGo;
					leftPart = line.substring(0, firstDown);
					middlePart = String.valueOf(line.charAt(firstDown));
					middlePart = Printer.decorate(middlePart, generateFirstDownModifiers(), generateFieldModifiers());
					rightPart = line.substring(firstDown + 1);
					line = String.format("%s%s%s", leftPart, middlePart, rightPart);
				}

				leftPart = line.substring(0, scrimmage);
				middlePart = String.valueOf(line.charAt(scrimmage));
				middlePart = Printer.decorate(middlePart, generateScrimmageModifiers(), generateFieldModifiers());
				rightPart = line.substring(scrimmage + 1);
				line = String.format("%s%s%s", leftPart, middlePart, rightPart);
			} else {
				leftPart = line.substring(0, scrimmage);
				middlePart = String.valueOf(line.charAt(scrimmage));
				middlePart = Printer.decorate(middlePart, generateScrimmageModifiers(), generateFieldModifiers());
				rightPart = line.substring(scrimmage + 1);
				line = String.format("%s%s%s", leftPart, middlePart, rightPart);

				if (down != 0) {
					firstDown -= toGo;
					leftPart = line.substring(0, firstDown);
					middlePart = String.valueOf(line.charAt(firstDown));
					middlePart = Printer.decorate(middlePart, generateFirstDownModifiers(), generateFieldModifiers());
					rightPart = line.substring(firstDown + 1);
					line = String.format("%s%s%s", leftPart, middlePart, rightPart);
				}
			}
			output.append(line);
		}
		output.append(String.format("%-127s", lowerSideline.toString()));

		return Printer.decorate(output.toString(), generateFieldModifiers());
	}
	
	private String generateModifiers (boolean current) {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);

		if (current) {
			modifiers.append(Printer.ANSI_WHITE_BOLD);
		}

		return modifiers.toString();
	}

	private String generateFirstDownModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		if (down == 4) {
			modifiers.append(Printer.ANSI_BACK_RED);
		} else {
			modifiers.append(Printer.ANSI_BACK_YELLOW);
		}
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}

	private String generateScrimmageModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_BACK_BLUE);
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}

	private String generateFieldModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_BACK_GREEN);
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}

	private int yardLineToInt (String yardLine, String home, String guest) {
		if (yardLine.isEmpty()) {
			return -1;
		}

		String num = yardLine.replaceAll("\\D", "");
		int asInt = Integer.parseInt(num);

		if (yardLine.startsWith(guest)) {
			return asInt;
		} else if (yardLine.startsWith(home)) {
			return 100 - asInt;
		} else {
			return 50;
		}
	}
}
