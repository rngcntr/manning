package control;

import java.net.*;
import java.io.*;

public class NetworkController {

	private ManningController manControl;

	public NetworkController (ManningController manControl) {
		this.manControl = manControl;
	}

	public String get (String url) {
		StringBuilder output = new StringBuilder();

		URL nflWeb = null;
		try {
			nflWeb = new URL(url);
		} catch(MalformedURLException e) {
			System.err.println("Unable to resolve web URL.");
			return "";
		}
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(nflWeb.openStream()));
		} catch(IOException ioex) {
			System.err.println("Unable to read from web service.");
			return "";
		}

		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null) {
				output.append(inputLine);
				output.append(System.getProperty("line.separator"));
			}
			in.close();
		} catch(IOException ioex) {
			System.err.println("Unable to read from web service.");
			return "";
		}

		return output.toString();
	}

}
