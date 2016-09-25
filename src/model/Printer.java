package model;

import java.text.SimpleDateFormat;
import java.util.Date;

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

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
}
