package de.bht.jvr.portals.util;

import java.util.ArrayList;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.portals.Teleporter;

public class TeleporterList {
	private static ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
	
	public static void check(GroupNode node){
		
		ArrayList<SceneNode> list =  (ArrayList<SceneNode>) node.getChildNodes();
		for(Teleporter tele : teleporters)
		{
			for(SceneNode scene : list)
			{
				tele.getPickPoint(scene, 5);
			}
		}
	}

	public static void add(Teleporter teleporter) {
		teleporters.add(teleporter);
	}
}
