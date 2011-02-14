package de.bht.jvr.portals;

public class PortalConnector {
	
	public static void connect(Teleporter portal1, Teleporter portal2) {
		portal1.setPortalExit(portal2);
		portal2.setPortalExit(portal1);
	}
}
