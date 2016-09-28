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

	public String toString (int width, boolean observed) {
		String output = Printer.breakLines(desc, width);
		output = Printer.fit(output, width, 3);
		return Printer.decorate(output, generateModifiers(observed));
	}
	
	private String generateModifiers (boolean observed) {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);

		if (observed) {
			modifiers.append(Printer.ANSI_WHITE_BOLD);
		}

		return modifiers.toString();
	}
}
