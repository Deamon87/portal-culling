package de.bht.jvr.portals;

import java.util.ArrayList;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.rendering.DrawList;

public class PortalList extends DrawList{
	private static ArrayList<Portal> portals = new ArrayList<Portal>();
	
	public static void update(CameraNode camera) {
		for (Portal portal : portals) {
			portal.update(camera);
		}
	}
	
	public static void add(Portal portal) {
		portals.add(portal);
	}
	
	public static void remove(Portal portal) {
		portals.remove(portal);
	}
}
