package de.bht.jvr.portals;

import java.util.ArrayList;

import de.bht.jvr.core.CameraNode;

public class PortalList {
	private static ArrayList<Portal> portals = new ArrayList<Portal>();
	
	public static void update(CameraNode camera, double moveSpeed) {
		for (Portal portal : portals) {
			portal.update(camera, moveSpeed);
		}
	}
	
	public static void render() {
		for (Portal portal : portals) {
			portal.render();
		}		
	}
	
	public static void add(Portal portal) {
		portals.add(portal);
	}
	
	public static void remove(Portal portal) {
		portals.remove(portal);
	}
}
