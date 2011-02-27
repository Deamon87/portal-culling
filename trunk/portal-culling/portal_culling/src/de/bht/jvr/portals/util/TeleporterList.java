package de.bht.jvr.portals.util;

import java.util.ArrayList;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.portals.Teleporter;

/**
 * the teleporter list class
 * 
 * @author Christopher Sierigk
 *
 */
public class TeleporterList {
	
	/** list of teleporters */
	private static ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
	
	/**
	 * Checks if scene node was teleported
	 * 
	 * @param node
	 * 			the scene node
	 * @return true, if node was teleported
	 */
	public static boolean check(SceneNode node){
		boolean teleport = false;
		
		for(Teleporter tele : teleporters)
		{
			if(tele.checkDist(node, 3))
			{
				teleport = true;
			}
		}
		
		return teleport;
	}

	/**
	 * Add a teleporter to the list
	 * 
	 * @param teleporter
	 * 				the teleporter to add
	 */
	public static void add(Teleporter teleporter) {
		teleporters.add(teleporter);
	}
	
	/**
	 * Remove a teleporter from the list
	 * 
	 * @param teleporter
	 * 				the teleporter to remove
	 */
	public static void remove(Teleporter teleporter) {
		teleporters.remove(teleporter);
	}
}
