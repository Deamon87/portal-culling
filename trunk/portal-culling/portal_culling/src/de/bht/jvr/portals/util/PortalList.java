package de.bht.jvr.portals.util;

import java.util.ArrayList;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.portals.Portal;

/**
 * the portal list class
 * 
 * @author Christopher Sierigk
 *
 */
public class PortalList {
	
	/** list of portals */
	private static ArrayList<Portal> portals = new ArrayList<Portal>();
	
	/**
	 * Updating every portal for each frame
	 * 
	 * @param camera
	 * 			the user camera
	 * @param moveSpeed
	 * 			the moving speed of the user camera
	 */
	public static void update(CameraNode camera, double moveSpeed) {
		for (Portal portal : portals) {
			portal.update(camera, moveSpeed);
		}
	}
	
	/**
	 * Render every portal for the scene
	 */
	public static void render() {
		for (Portal portal : portals) {
			portal.render();
		}		
	}
	
	/**
	 * Add a portal to the list
	 * 
	 * @param portal
	 * 			the portal to add
	 */
	public static void add(Portal portal) {
		portals.add(portal);
	}
	
	/**
	 * Remove a portal from the list
	 * 
	 * @param portal
	 * 			the portal to remove
	 */
	public static void remove(Portal portal) {
		portals.remove(portal);
	}
}
