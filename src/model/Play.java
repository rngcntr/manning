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
		right = String.format(" %s", right);
		right = Printer.breakLines(right, width - 9);
		right = Printer.fit(right, width - 9, 3);
		String left = String.format("[%5s]", time);
		left = Printer.fit(left, 8, 3);
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
			String.format("%-127s\n", upperSideline.toString()),
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
			"   ╚═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╧═════════╝   \n",
			String.format("%-127s", lowerSideline.toString())};

		StringBuilder output = new StringBuilder();

		for (String line : field) {
			output.append(line);
		}

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
