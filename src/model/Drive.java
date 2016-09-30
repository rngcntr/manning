package model;

import org.json.simple.*;
import java.util.*;

public class Drive {

	private String team = "";

	private ArrayList<Play> plays;

	private String result = "";
	private long yards = 0L;
	private long numPlays = 0L;
	private long quarter = 0L;

	public static Drive fromJSON (JSONObject jsonObject) {
		Drive output = new Drive();

		try {
			output.team = (String) jsonObject.get("posteam");
			output.result = (String) jsonObject.get("result");
			output.yards = (long) jsonObject.get("ydsgained");
			output.numPlays = (long) jsonObject.get("numplays");
			output.quarter = (long) jsonObject.get("qtr");
			
			JSONObject driveStart = (JSONObject) jsonObject.get("start");
			String startLine = (String) driveStart.get("yrdln");

			JSONObject jsonPlays = (JSONObject) jsonObject.get("plays");
			output.plays = new ArrayList<Play>();
			ArrayList<String> playKeys = new ArrayList<String>();
			
			for (Object o : jsonPlays.keySet()) {
				playKeys.add((String) o);
			}

			Collections.sort(playKeys, new Comparator<String>() {
				public int compare(String o1, String o2) {
					return extractInt(o1) - extractInt(o2);
				}

				int extractInt(String s) {
					String num = s.replaceAll("\\D", "");
					// return 0 if no digits found
					return num.isEmpty() ? 0 : Integer.parseInt(num);
				}
			});

			for (Object key : playKeys) {
				JSONObject jsonPlay = (JSONObject) jsonPlays.get((String) key);
				Play newPlay = Play.fromJSON(jsonPlay);
				newPlay.setStart(startLine);
				output.plays.add(newPlay);
			}
		} catch (NullPointerException npex) {
			npex.printStackTrace();
			System.err.println("Unable to parse Drive from JSON");
			return null;
		}

		return output; 
	}

	public String toString (int width, boolean current, int observedPlay) {
		StringBuilder output = new StringBuilder();
		String left = String.format("[%3s] %3s: %2d plays, %3d yards ",
				Printer.numberAsString(quarter), team, numPlays, yards);
		
		int space = width - left.length() - result.length();
		if (space < 0) {
			return "";
		}

		String header = String.format("%s%s%s", left, Printer.generateSpace(space), result);
		StringBuilder content = new StringBuilder();
		
		Play nextPlay = plays.get(plays.size() - observedPlay - 1);
		content.append(String.format("\n\n%s", nextPlay.toString(width - 3, current && observedPlay == 0)));

		if (current) {
			header = Printer.decorate(header, generateDefaultModifiers());
		} else {
			header = Printer.decorate(header, generateUnfocusedModifiers());
		}

		output.append(header);
		output.append(content.toString());

		return output.toString();
	}

	public String getField (int observedPlay, String home, String guest) {
		Play play = plays.get(plays.size() - observedPlay - 1);
		return play.getField(home, guest);
	}

	public int getPlayCount () {
		return plays.size();
	}

	private String generateDefaultModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_BACK_BLACK);
		modifiers.append(Printer.ANSI_WHITE_BOLD);

		return modifiers.toString();
	}

	private String generateUnfocusedModifiers () {
		StringBuilder modifiers = new StringBuilder();

		modifiers.append(Printer.ANSI_RESET);
		modifiers.append(Printer.ANSI_BACK_BLACK);
		modifiers.append(Printer.ANSI_WHITE);

		return modifiers.toString();
	}
}
