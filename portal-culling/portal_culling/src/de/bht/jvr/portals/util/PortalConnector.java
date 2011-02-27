package de.bht.jvr.portals.util;

import de.bht.jvr.portals.Teleporter;

/**
 * the portal connector class
 * 
 * @author Christopher Sierigk
 *
 */
public class PortalConnector {
	
	/**
	 * Connecting 2 portals with each other
	 * 
	 * @param portal1
	 * 			the first portal
	 * @param portal2
	 * 			the second portal
	 */
	public static void connect(Teleporter portal1, Teleporter portal2) {
		portal1.setPortalExit(portal2);
		portal2.setPortalExit(portal1);
	}
}
