package control;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import org.json.simple.*;
import org.json.simple.parser.*;

public class NetworkController {

	private ManningController manControl;

	private HashMap<String, String> modified;
	private HashMap<String, String> cache;

	public NetworkController (ManningController manControl) {
		this.manControl = manControl;

		modified = new HashMap<String, String>();
		cache = new HashMap<String, String>();
	}

	public String get (String url) {
		StringBuilder output = new StringBuilder();

		URL webURL = null;
		try {
			webURL = new URL(url);
			String lastModified = modified.get(url);
			URLConnection connection = webURL.openConnection();
			connection.setRequestProperty("If-Modified-Since", lastModified);
			String newLastModified = connection.getHeaderField("Last-Modified");

			// check whether the cached version is valid
			if (newLastModified.equals(lastModified)) {
				return cache.get(url);
			} else {
				// site has changed, refresh information
				modified.put(url, newLastModified);
			}
		} catch (NullPointerException npex) {
			return cache.get(url);
		} catch (MalformedURLException mfuex) {
			System.err.println("Unable to resolve web URL.");
			return cache.get(url);
		} catch (IOException ioex) {
			System.err.println("Unable to connect to web URL.");
			return cache.get(url);
		}
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(webURL.openStream()));
		} catch(IOException ioex) {
			System.err.println("Unable to read from web service.");
			return cache.get(url);
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
			return cache.get(url);
		}

		cache.put(url, output.toString());
		return output.toString();
	}

	public JSONObject parse (String json) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			return jsonObject;
		} catch (ParseException pex) {
			System.err.println("Unable to parse JSON String.");
			return null;
		}
	}

}
