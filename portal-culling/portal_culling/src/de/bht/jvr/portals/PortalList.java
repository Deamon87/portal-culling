package de.bht.jvr.portals;

import java.util.ArrayList;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.pipeline.Pipeline;

public class PortalList {
	private static ArrayList<Portal> portals = new ArrayList<Portal>();
	
	public static void update(CameraNode camera) {
		for (Portal portal : portals) {
			portal.update(camera);
		}
	}
	
	public static void render(Pipeline p) {
		for (Portal portal : portals) {
			p.bindColorBuffer("jvr_PortalTexture", portal.getName() + "FBO", 0);
			p.drawGeometry("AMBIENT", portal.getName() + "Mat");
		}		
	}
	
	public static void add(Portal portal) {
		portals.add(portal);
	}
	
	public static void remove(Portal portal) {
		portals.remove(portal);
	}
}
