package model;

import org.json.simple.*;

public class Play {

	private long quarter = 0L;
	private long down = 0L;
	private String time = "";
	private String yardLine = "";
	private long toGo = 0L;
	private long yards = 0L;

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
			
			output.desc = (String) jsonObject.get("desc");
			output.note = (String) jsonObject.get("note");
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse Play from JSON");
			return null;
		}

		return output; 
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
	
	private String generateModifiers (boolean current) {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);

		if (current) {
			modifiers.append(Printer.ANSI_WHITE_BOLD);
		}

		return modifiers.toString();
	}
}
