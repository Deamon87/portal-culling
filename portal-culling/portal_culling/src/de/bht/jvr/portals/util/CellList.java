package de.bht.jvr.portals.util;

import java.util.ArrayList;

import de.bht.jvr.core.SceneNode;
import de.bht.jvr.portals.culling.Cell;

/**
 * the cell list class
 * 
 * @author Christopher Sierigk
 *
 */
public class CellList {

	/** list of cells */
	private static ArrayList<Cell> cells = new ArrayList<Cell>();
	
	/**
	 * Checks the cells which contains the scene node
	 * 
	 * @param node
	 * 			the scene node
	 * @return the name of the cell containing the node 		
	 */
	public static String checkCell(SceneNode node) {
		String name = "";
		
		for(Cell cell : cells) {
			if(cell.contains(node))
			{
				name = cell.getName();
			}
		}
		
		return name;
	}
	
	/**
	 * Adding a cell to the list
	 * 
	 * @param cell
	 * 			the cell to add
	 */
	public static void add(Cell cell) {
		cells.add(cell);
	}
	
	/**
	 * Removing a cell from the list
	 * 
	 * @param cell
	 * 			the cell to remove
	 */
	public static void remove(Cell cell) {
		cells.remove(cell);
	}
}
