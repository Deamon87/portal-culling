package de.bht.jvr.portals;

import java.util.ArrayList;

import de.bht.jvr.core.SceneNode;

public class CellList {

	private static ArrayList<Cell> cells = new ArrayList<Cell>();
	
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
	
	public static void add(Cell cell) {
		cells.add(cell);
	}
	
	public static void remove(Cell cell) {
		cells.remove(cell);
	}
}
