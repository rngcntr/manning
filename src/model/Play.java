package model;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Play {

	private long quarter = 0L;
	private long down = 0L;
	private String time = "";
	private String yardLine = "";
	private long toGo = 0L;
	private long yards = 0L;

	private String desc = "";
	private String note = "";

	public static Play fromJSON (String json) {
		Play output = new Play();

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			output.quarter = (long) jsonObject.get("qtr");
			output.down = (long) jsonObject.get("down");
			output.time = (String) jsonObject.get("time");
			output.yardLine = (String) jsonObject.get("yrdln");
			output.toGo = (long) jsonObject.get("ydstogo");
			output.yards = (long) jsonObject.get("ydsnet");
			
			output.desc = (String) jsonObject.get("desc");
			output.note = (String) jsonObject.get("note");
		} catch (ParseException pex) {
			System.err.println("Unable to parse Play from JSON");
			return null;
		} catch (NullPointerException npex) {
			System.err.println("Unable to parse Play from JSON");
			return null;
		}

		return output; 
	}
}
