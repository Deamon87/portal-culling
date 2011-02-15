package de.bht.jvr.portals.util;

import java.util.ArrayList;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.portals.Teleporter;

public class TeleporterList {
	private static ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
	
	public static void check(SceneNode node){
		
		for(Teleporter tele : teleporters)
		{
			tele.getPickPoint(node, 5);
		}
	}

	public static void add(Teleporter teleporter) {
		teleporters.add(teleporter);
	}
}
