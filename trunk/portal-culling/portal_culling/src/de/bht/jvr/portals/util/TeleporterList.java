package de.bht.jvr.portals.util;

import java.util.ArrayList;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.portals.Teleporter;

public class TeleporterList {
	private static ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
	
	public static boolean check(SceneNode node){
		boolean teleport = false;
		
		for(Teleporter tele : teleporters)
		{
			if(tele.getPickPoint(node, 3))
			{
				teleport = true;
			}
		}
		
		return teleport;
	}
	
	public static void checkPhysics(){
		for(Teleporter tele : teleporters)
		{
			tele.checkPhysics();
		}
	}

	public static void add(Teleporter teleporter) {
		teleporters.add(teleporter);
	}
}
