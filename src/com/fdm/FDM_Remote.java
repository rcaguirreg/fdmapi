package com.fdm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Class used to comunicate with Free Download Managers Remote Server This is
 * really limited by what FDM returns and the limited functions
 * 
 * There are three main functions
 * 
 * 
 * @see this.addDownload Get List of complete
 * @see this.getCompletedDownloads Get List of active
 * @see this.getActiveDownloads
 * 
 * @author Tyron Gower
 * 
 */
public class FDM_Remote {
	private static String _protocol;
	private static int _port;
	private static String _host;
	private static String _username;
	private static String _password;

	/**
	 * Constructor for FDM connection. Use this constructor if you have setup
	 * authentication on your FDM Server.
	 * 
	 * @param host
	 *            - The host machine or domain name. (Do not add "http://".
	 * @param port
	 *            - The port configured in FDM Remote Server.
	 * @param username
	 *            - The username you specified.
	 * @param password
	 *            - The password you specified.
	 * 
	 * 
	 */
	public FDM_Remote(HTTPProtocol protocol, String host, int port,
			String username, String password) {

		switch (protocol) {
		case HTTP:
			FDM_Remote._protocol = "http://";
			break;
		case HTTPS:
			FDM_Remote._protocol = "https://";
			break;
		default:
			FDM_Remote._protocol = "http://";
			break;

		}
		FDM_Remote._host = host;
		FDM_Remote._port = port;
		FDM_Remote._username = username;
		FDM_Remote._password = password;
	}

	/**
	 * Constructor for FDM connection. Use this constructor if you have not
	 * setup authentication on your FDM Server.
	 * 
	 * @param host
	 *            - The host machine or domain name. (Do not add "http://".
	 * @param port
	 *            - The port configured in FDM Remote Server.
	 * 
	 */
	public FDM_Remote(HTTPProtocol protocol, String host, int port) {
		switch (protocol) {
		case HTTP:
			FDM_Remote._protocol = "http://";
			break;
		case HTTPS:
			FDM_Remote._protocol = "https://";
			break;
		default:
			FDM_Remote._protocol = "http://";
			break;

		}
		FDM_Remote._host = host;
		FDM_Remote._port = port;

	}

	/**
	 * This will return an array of active downloads
	 * 
	 * @return String[] - The array of active downloads.
	 */
	public String getActiveDownloads() {

		return makeRequest("/");
	}

	/**
	 * The returns the array of completed downloads.
	 * 
	 * [0] - File Name [1] - File Size [2] - Url
	 * 
	 * @return - The array of completed downloads.
	 */
	public String getCompletedDownloads() {

		String tableString = makeRequest("compdlds.req");
		return tableString;
	}

	/**
	 * used to add a single url to the downloads.
	 * 
	 * @param url
	 *            - Must be a fully qaulified url and not require a login to
	 *            download.
	 * 
	 * @return - True if added, and false if failed.
	 */
	public boolean addDownload(String url) {

		if ( url.startsWith("http://") ||  url.startsWith("https://")){
		return Boolean.getBoolean(makeRequest("adddownload.req?URL=" + url));
		}
		else {
			return false;
		}
	}

	/**
	 * 
	 * @param urls
	 *            - An array of Urls, Must be fully qaulified Urls not needing
	 *            authentication to access.
	 * 
	 * @return - Will return true if they all add correctly and will return
	 *         false as soon as one fails. This means if one fails nothing after
	 *         that will be added.
	 * 
	 */
	public boolean addMultipleDownloads(String[] urls) {

		
		for (int i = 0; i < urls.length; i++) {
			if (!Boolean.getBoolean(makeRequest("/adddownload.req?URL="
					+ urls[i]))) {
				return false;
			}
		}
		return true;

	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String makeRequest(String req) {
		String requestUrl;
		
		if (! req.startsWith("/")){
			req = "/" + req;
		}
		if (_username == null) {
			requestUrl = _protocol + _host + ":" + _port  + req;
		} else {
			requestUrl = _protocol + _username + ":" + _password + "@" + _host
					+ ":" + _port + req;
		}

		String result = "";
		try {

			URL url = new URL(requestUrl.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				result += inputLine;
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
