package gov.lbnl.visit.swt;

import java.util.HashMap;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class VisItSwtConnectionManager {

	private HashMap<String, VisItSwtConnection> conns;

	/**
	 * ! static manager instance..
	 */
	private static VisItSwtConnectionManager manager = new VisItSwtConnectionManager();

	/**
	 * !
	 * 
	 */
	private VisItSwtConnectionManager() {
		conns = new HashMap<String, VisItSwtConnection>();
	}

	public static VisItSwtConnectionManager instance() {
		return manager;
	}

	public static HashMap<String, VisItSwtConnection> getConnMap() {
		return manager.conns;
	}

	public static boolean hasConnection(String key) {
		return manager.conns.containsKey(key);
	}

	public static VisItSwtConnection getConnection(String key) {

		if (manager.conns.containsKey(key)) {
			return manager.conns.get(key);
		}

		return null;
	}

	public static VisItSwtConnection createConnection(String key,
			Display display, HashMap<String, String> inputMap) {

		try {
			VisItSwtConnection vizConnection = new VisItSwtConnection(
					new Shell(display));

			String username = inputMap.get("username");
			String password = inputMap.get("password");
			int windowId = Integer.parseInt(inputMap.get("windowId"));
			int windowWidth = Integer.parseInt(inputMap.get("windowWidth"));
			int windowHeight = Integer.parseInt(inputMap.get("windowHeight"));
			String gateway = inputMap.get("gateway");
			int localGatewayPort = inputMap.get("localGatewayPort").isEmpty() ? 0
					: Integer.parseInt(inputMap.get("localGatewayPort"));
			String useTunneling = inputMap.get("useTunneling");
			String url = inputMap.get("url");
			int port = Integer.parseInt(inputMap.get("port"));
			String visDir = inputMap.get("visDir");
			boolean isRemote = Boolean.valueOf(inputMap.get("isRemote"));

			// Set the parameters on the widget
			vizConnection.setParameters(username, password,
					VisItSwtConnection.VISIT_CONNECTION_TYPE.IMAGE,
					windowWidth, windowHeight, windowId);

			// Setup a remote gateway if needed
			if (!gateway.isEmpty()) {
				vizConnection.setGateway(gateway, localGatewayPort);
			}

			// Enable tunneling if needed
			vizConnection.useTunneling(Boolean.valueOf(useTunneling));

			// Launch the VisIt widget
			boolean result = vizConnection.launch(url, port, password, visDir,
					isRemote);

			if (result) {
				manager.conns.put(key, vizConnection);
				return vizConnection;
			}
		} catch (Exception e) {
			System.err.println("VisItSwtConnectionManager Message: "
					+ "Error during VisItSwtConnection construction: "
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
