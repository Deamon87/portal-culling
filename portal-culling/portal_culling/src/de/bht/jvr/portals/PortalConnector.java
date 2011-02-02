package de.bht.jvr.portals;

public class PortalConnector {
	
	public static void connect(Portal portal1, Portal portal2) {
		portal1.setPortalExit(portal2);
		portal2.setPortalExit(portal1);
	}
}
