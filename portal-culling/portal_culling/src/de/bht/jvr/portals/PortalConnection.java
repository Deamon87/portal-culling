package de.bht.jvr.portals;

public class PortalConnection {
	
	public static void connect(Portal portal1, Portal portal2) {
		portal1.setPortalExit(portal2);
		portal2.setPortalExit(portal1);
	}
}
